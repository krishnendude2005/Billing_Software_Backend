package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.entity.UserEntity;
import com.Krishnendu.BillingSoftware.io.UserRequest;
import com.Krishnendu.BillingSoftware.io.UserResponse;
import com.Krishnendu.BillingSoftware.repository.UserEntityRepo;
import com.Krishnendu.BillingSoftware.service.UserService;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserEntityRepo userEntityRepo;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest request) {
        //Convert request object into entity object
        UserEntity newUser = convertToEntity(request);

        // save the user in the DB
        newUser = userEntityRepo.save(newUser);

        //Convert into response object and return
        return convertToResponse(newUser);

    }

    private UserResponse convertToResponse(UserEntity newUser) {
        return
                UserResponse.builder()
                        .userId(newUser.getUserId())
                        .name(newUser.getName())
                        .email(newUser.getEmail())
                        .role(newUser.getRole())
                        .createdAt(newUser.getCreatedAt())
                        .updatedAt(newUser.getUpdatedAt())
                        .build();
    }

    private UserEntity convertToEntity(UserRequest request) {
        return
                UserEntity.builder()
                        .userId(UUID.randomUUID().toString())
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole().toUpperCase())
                        .build();

    }

    @Override
    public String getUserRole(String email) {
        UserEntity existingUser = userEntityRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return existingUser.getRole();
    }

    @Override
    public List<UserResponse> readAllUsers() {
        return userEntityRepo.findAll()
                .stream()
                .map(user -> convertToResponse(user))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String id) {
        UserEntity existingUser = userEntityRepo.findByUserId(id).orElseThrow(() -> new UsernameNotFoundException(id));

        if(existingUser.getRole().equals("ADMIN")){
            throw new IllegalStateException("Admin Cannot be deleted");
        }

        userEntityRepo.delete(existingUser);
    }
}
