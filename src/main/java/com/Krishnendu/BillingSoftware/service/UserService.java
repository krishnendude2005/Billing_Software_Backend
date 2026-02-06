package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.UserRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    void createUser(UserRequest request);
}
