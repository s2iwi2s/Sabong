package com.s2i.sabong.service.services.impl;

import com.s2i.sabong.data.domain.UserEntity;
import com.s2i.sabong.data.repository.UserRepository;
import com.s2i.sabong.service.dto.UserDTO;
import com.s2i.sabong.service.mapper.UserMapper;
import com.s2i.sabong.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        log.debug("Request to save userDTO : {}", userDTO);
//        return Optional.of(userDTO).map(userMapper::toEntity)
//                .map(userRepository::save)
//                .map(userMapper::toDto);
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity = userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    public Optional<UserDTO> partialUpdate(UserDTO userDTO) {
        log.debug("Request to partially update Student : {}", userDTO);

        return userRepository
                .findById(userDTO.getId())
                .map(
                        existingStudent -> {
                            userMapper.partialUpdate(existingStudent, userDTO);
                            return existingStudent;
                        }
                )
                .map(userRepository::save)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    public Optional<UserDTO> findOneByLogin(String login) {
        return userRepository.findOneByLogin(login).map(userMapper::toDto);
    }
}
