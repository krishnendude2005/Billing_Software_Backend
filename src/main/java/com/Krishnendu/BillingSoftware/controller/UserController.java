package com.Krishnendu.BillingSoftware.controller;

import com.Krishnendu.BillingSoftware.entity.UserEntity;
import com.Krishnendu.BillingSoftware.io.UserRequest;
import com.Krishnendu.BillingSoftware.io.UserResponse;
import com.Krishnendu.BillingSoftware.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest user) {

        try{
            userService.createUser(user);
        }catch(Exception ex){
            return ResponseEntity.badRequest().body("Unable to register user");
        }

        return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("User registered successfully");

    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {

       List<UserResponse> users = userService.readAllUsers();
       return
               ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
