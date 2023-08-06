package com.s2i.sabong.security.services.impl;

import com.s2i.sabong.data.repository.UserPasswordRepository;
import com.s2i.sabong.data.repository.UserRepository;
import com.s2i.sabong.security.exception.UserNotActivatedException;
import com.s2i.sabong.security.services.UserDetailsService;
import com.s2i.sabong.service.dto.UserDTO;
import com.s2i.sabong.service.mapper.UserMapper;
import com.s2i.sabong.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService, org.springframework.security.core.userdetails.UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserPasswordRepository userPasswordRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserService userService,
                                  UserRepository userRepository,
                                  UserPasswordRepository userPasswordRepository,
                                  UserMapper userMapper,
                                  PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userPasswordRepository = userPasswordRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        log.debug("Service to save userDTO : {}", userDTO);
        userDTO.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        return userService.save(userDTO);
    }

    public void savePassword(Long userId, String password) {
        log.debug("Service to save password of userId  : {}", userId);

        userPasswordRepository.findById(userId)
                .map(user -> user.setPasswordHash(this.passwordEncoder.encode(password)))
                .map(userPasswordRepository::save);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.debug("Authenticating {}", login);
        return userService.findOneWithAuthoritiesByLogin(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String login, UserDTO user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + login + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
    }
}
