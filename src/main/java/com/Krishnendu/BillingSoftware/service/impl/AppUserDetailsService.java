package com.Krishnendu.BillingSoftware.service.impl;

import com.Krishnendu.BillingSoftware.entity.UserEntity;
import com.Krishnendu.BillingSoftware.repository.UserEntityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserEntityRepo userEntityRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         UserEntity existingUser = userEntityRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found Email : " + email));

         return new User(existingUser.getEmail(), existingUser.getPassword(), Collections.singleton(new SimpleGrantedAuthority(existingUser.getRole())));
    }
}
