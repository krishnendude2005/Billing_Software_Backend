package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.io.AuthRequest;
import com.Krishnendu.BillingSoftware.io.AuthResponse;
import com.Krishnendu.BillingSoftware.service.impl.AppUserDetailsService;
import com.Krishnendu.BillingSoftware.service.impl.AuthServiceImpl;
import com.Krishnendu.BillingSoftware.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.AuthProvider;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final AuthServiceImpl authService;
    private final JWTUtils jwtUtils;

    // Endpoint to encode the password
    @PostMapping("/encode")
    public String encodePassword(@RequestBody Map<String, String> request) {
        return passwordEncoder.encode(request.get("password"));
    }


    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        //First, authenticate the user
        try {
            authenticate(request.getEmail(), request.getPassword());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        //Now, generate the jwt token

        //get the userDetails from the DB
        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());


        String JWTToken = jwtUtils.generateJWTToken(userDetails);


        //TODO: fetch the role from repo
        return new AuthResponse(request.getEmail(), "USER", JWTToken);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new BadCredentialsException("Username or password is disabled");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
