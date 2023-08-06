package com.s2i.sabong.service.mapper;

import com.s2i.sabong.data.domain.UserEntity;
import com.s2i.sabong.service.dto.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void init() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testToEntity() {
        UserDTO userDTO = createDTO();
        UserEntity userEntity = userMapper.toEntity(userDTO);
        assertThat(Optional.of(userEntity).orElse(null).getId()).isNotNull();

        testAssertEquals(userEntity, userDTO);
    }

    @Test
    void testToDto() {
        UserEntity userEntity = createEntity();
        UserDTO userDTO = userMapper.toDto(userEntity);

        testAssertEquals(userEntity, userDTO);
    }

    private void testAssertEquals(UserEntity userEntity, UserDTO userDTO) {
        assertEquals(userEntity.getLogin(), userDTO.getLogin());
        assertEquals(userEntity.getPassword(), userDTO.getPassword());
        assertEquals(userEntity.isActivated(), userDTO.isActivated());
        assertEquals(userEntity.getEmail(), userDTO.getEmail());
        assertEquals(userEntity.getFirstName(), userDTO.getFirstName());
        assertEquals(userEntity.getLastName(), userDTO.getLastName());
    }

    private UserDTO createDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setLogin("johndoe");
        userDTO.setPassword(RandomStringUtils.random(60));
        userDTO.setActivated(true);
        userDTO.setEmail("johndoe@localhost");
        userDTO.setFirstName("john");
        userDTO.setLastName("doe");
        return userDTO;
    }


    public UserEntity createEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setLogin("johndoe");
        userEntity.setPassword(RandomStringUtils.random(60));
        userEntity.setActivated(true);
        userEntity.setEmail("johndoe@localhost");
        userEntity.setFirstName("john");
        userEntity.setLastName("doe");
        return userEntity;
    }
}