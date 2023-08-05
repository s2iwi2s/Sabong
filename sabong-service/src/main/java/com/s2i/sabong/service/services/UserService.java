package com.s2i.sabong.service.services;

import com.s2i.sabong.service.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    UserDTO save(UserDTO userDTO);
    public Optional<UserDTO> partialUpdate(UserDTO userDTO);
    public Optional<UserDTO> findOne(Long id);
    public Optional<UserDTO> findOneByLogin(String login);
}
