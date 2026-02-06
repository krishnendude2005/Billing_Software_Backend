package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.UserRequest;
import com.Krishnendu.BillingSoftware.io.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    String getUserRole(String email);
    List<UserResponse> readAllUsers();
    void deleteUser(String id);
}
