package com.s2i.sabong.security.services;

import com.s2i.sabong.service.dto.UserDTO;

public interface UserDetailsService {
    UserDTO save(UserDTO userDTO);
}
