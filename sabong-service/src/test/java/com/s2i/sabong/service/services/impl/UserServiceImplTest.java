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
        UserDTO savedUser = userService.save(userDTO);
        assertThat(Optional.of(savedUser).orElse(null).getId()).isNotNull();

        Optional<UserEntity> maybeUser = userRepository.findOneByLogin(DEFAULT_LOGIN);
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getId()).isEqualTo(savedUser.getId());
        assertThat(maybeUser.orElse(null).getLogin()).isEqualTo(savedUser.getLogin());
        assertThat(maybeUser.orElse(null).getEmail()).isEqualTo(savedUser.getEmail());
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