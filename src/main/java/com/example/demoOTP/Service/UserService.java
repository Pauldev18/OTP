package com.example.demoOTP.Service;


import com.example.demoOTP.Entity.Users;
import com.example.demoOTP.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username);
        GrantedAuthority authority = new SimpleGrantedAuthority(users.getRole());
        UserDetails userDetails = (UserDetails) new User(users.getUserName(),
                users.getPassWord(), Arrays.asList(authority));

        return userDetails;
    }
}
