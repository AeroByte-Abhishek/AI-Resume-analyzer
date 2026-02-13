package com.example.RapidResume.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tika.Tika;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/resume")
//@CrossOrigin("*")
public class ResumeController {
    private final ChatClient chatClient;
    private final Tika tika = new Tika();

    public ResumeController(OpenAiChatModel openAiChatModel) {
        this.chatClient = ChatClient.create(openAiChatModel);
    }

    @PostMapping("/analyzer")
    public Map<String, Object> analyzer(@RequestParam("file") MultipartFile file) throws Exception {
        
        String content = tika.parseToString(file.getInputStream());
        
        String prompt = """
        Analyze this resume text:
        
        %s
        
        Based on the above resume:
        1. Extract key skills
        2. Rate overall resume quality (1-10)
        3. Suggest 3 improvements
        
        Reply in structured JSON format with keys: "score", "improvements", "skills"
        """.formatted(content);


        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return Map.of("Analysis", aiResponse);
    }
    
    @PostMapping("/atsChecker")
    public Map<String, Object> atsChecker(@RequestParam("file") MultipartFile file, @RequestParam("jd") String JobDescription) throws Exception {
        String content = tika.parseToString(file.getInputStream());

        String prompt = """
You are an automated Applicant Tracking System (ATS).

Analyze the resume strictly based on the provided text content.
You do NOT have access to the original file layout or formatting.

RESUME TEXT:
----------------
%s
----------------

JOB DESCRIPTION:
----------------
%s
----------------

RULES:
1. Use the Job Description as the primary reference.
2. Extract keywords ONLY from the Job Description.
3. Match resume text against those keywords (case-insensitive).
4. Count synonyms and common variations as matches.
5. Do NOT assume layout, formatting, tables, or visuals.
6. Do NOT infer skills not explicitly written.

SCORING:
- atsScore must be between 0 and 100.
- Score is based ONLY on:
  • Keyword coverage vs Job Description
  • Presence of relevant sections
  • Relevance of experience text

OUTPUT RULES:
- Output ONLY raw JSON
- No markdown
- No explanations
- Must start with { and end with }

REQUIRED JSON FORMAT:
{
  "atsScore": number,
  "sections": {
    "present": [],
    "missing": []
  },
  "keywords": {
    "found": [],
    "missing": []
  },
  "formattingIssues": [],
  "improvements": []
}
""".formatted(content, JobDescription);


        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parsed = mapper.readValue(aiResponse, new TypeReference<>() {});


        return Map.of("AtsChecker", parsed);
    }
}
