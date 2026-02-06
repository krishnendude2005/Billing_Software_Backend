package com.Krishnendu.BillingSoftware.service;

import com.Krishnendu.BillingSoftware.io.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    String getJWTKey(UserDetails userDetails);
}
