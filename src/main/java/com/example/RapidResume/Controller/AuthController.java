package com.example.RapidResume.Controller;

import com.example.RapidResume.Dto.LoginRequest;
import com.example.RapidResume.Dto.RegisterRequest;
import com.example.RapidResume.Entity.CustomUser;
import com.example.RapidResume.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationConfiguration authenticationConfiguration) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @PostMapping("/register")
    public Object register(@RequestBody RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity
                    .badRequest()               
                    .body("User Already Exists");
        }

        CustomUser user = new CustomUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        
        return ResponseEntity.ok("User Registered Successfully");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest request){
        try {
            
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
            );
            return ResponseEntity.ok("Login Successfully");
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Email or Password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
