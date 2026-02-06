package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.io.AuthRequest;
import com.Krishnendu.BillingSoftware.service.AuthService;
import com.Krishnendu.BillingSoftware.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private JWTUtils jwtUtils;
    @Override
    public String getJWTKey(UserDetails userDetails) {
        return jwtUtils.generateJWTToken(userDetails);
    }
}
