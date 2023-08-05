package com.s2i.sabong.service.services.impl;

import com.s2i.sabong.IntegrationTest;
import com.s2i.sabong.data.domain.UserEntity;
import com.s2i.sabong.data.repository.UserRepository;
import com.s2i.sabong.service.dto.UserDTO;
import com.s2i.sabong.service.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Transactional
class UserServiceImplTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String DEFAULT_LASTNAME = "doe";
    private static final String DEFAULT_USER = "wpidor";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    private UserDTO userDTO;
    @BeforeEach
    public void init() {
        userDTO = createDTO();
    }

    @Test
    @Transactional
    void testSave() {
        UserDTO savedUserDTO = userService.save(userDTO);
        assertThat(Optional.of(savedUserDTO).orElse(null).getId()).isNotNull();

        Optional<UserEntity> maybeUserEntity = userRepository.findOneByLogin(DEFAULT_LOGIN);
        assertThat(maybeUserEntity).isPresent();
        assertThat(maybeUserEntity.orElse(null).getId()).isEqualTo(savedUserDTO.getId());
        assertThat(maybeUserEntity.orElse(null).getLogin()).isEqualTo(savedUserDTO.getLogin());
        assertThat(maybeUserEntity.orElse(null).getEmail()).isEqualTo(savedUserDTO.getEmail());
    }

    @Test
    @Transactional
    void testUpdate() {
        UserDTO savedUserDTO = userService.save(userDTO);
        final String newLogin = DEFAULT_LOGIN + "X";
        savedUserDTO.setLogin(newLogin);
        savedUserDTO.setPassword(RandomStringUtils.random(60) + "X");
        savedUserDTO.setActivated(false);
        savedUserDTO.setEmail(DEFAULT_EMAIL + "X");
//        savedUserDTO.setFirstName(DEFAULT_FIRSTNAME + "X");
//        savedUserDTO.setLastName(DEFAULT_LASTNAME + "X");

        Optional<UserDTO> savedUserDTOOptional = userService.partialUpdate(savedUserDTO);
        assertThat(savedUserDTOOptional.orElse(null).getId()).isNotNull();

        Optional<UserEntity> maybeUser = userRepository.findOneByLogin(newLogin);
        assertThat(maybeUser).isPresent();
        assertThat(savedUserDTOOptional.orElse(null).getId()).isNotNull();
        assertThat(savedUserDTOOptional.orElse(null).getId()).isEqualTo(savedUserDTO.getId());

        assertThat(maybeUser.orElse(null).getLogin()).isNotEqualTo(userDTO.getLogin());
        assertThat(maybeUser.orElse(null).getPassword()).isNotEqualTo(userDTO.getPassword());
        assertThat(maybeUser.orElse(null).isActivated()).isNotEqualTo(userDTO.isActivated());
        assertThat(maybeUser.orElse(null).getEmail()).isNotEqualTo(userDTO.getEmail());

        assertThat(maybeUser.orElse(null).getFirstName()).isEqualTo(userDTO.getFirstName());
        assertThat(maybeUser.orElse(null).getLastName()).isEqualTo(userDTO.getLastName());
    }

    public UserDTO createDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setPassword(RandomStringUtils.random(60));
        userDTO.setActivated(true);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        return userDTO;
    }
}