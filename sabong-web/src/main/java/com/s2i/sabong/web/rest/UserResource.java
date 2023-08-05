package com.s2i.sabong.web.rest;

import com.s2i.sabong.service.dto.UserDTO;
import com.s2i.sabong.service.services.UserService;
import com.s2i.sabong.web.config.Constants;
import com.s2i.sabong.web.exceptions.BadRequestAlertException;
import com.s2i.sabong.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = Constants.URL_API_BASE + UserResource.URL_BASE)
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String URL_BASE = "/user";
    private static final String ENTITY_NAME = "user";

    private final UserService userService;

    private final String applicationName;

    public UserResource(
            @Value("${spring.application.name}") String applicationName,
                        UserService userService) {
        this.applicationName = applicationName;
        this.userService = userService;
    }
    @PostMapping("")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save UserDTO : {}", userDTO);
        if (userDTO.getLogin() == null) {
            throw new BadRequestAlertException("A new user cannot  have null login", ENTITY_NAME, "nulllogin");
        }
        if (userDTO.getEmail() == null) {
            throw new BadRequestAlertException("A new user cannot  have null email", ENTITY_NAME, "nullemail");
        }
        if (userDTO.getPassword() == null) {
            throw new BadRequestAlertException("A new user cannot  have null password", ENTITY_NAME, "nullpassword");
        }
        UserDTO result = userService.save(userDTO);
        return ResponseEntity
                .created(new URI(Constants.URL_API_BASE + UserResource.URL_BASE + "/" + result.getLogin()))
                .headers(HeaderUtil.createEntityCreationAlert(this.applicationName, true, ENTITY_NAME, userDTO.getLogin()))
                .body(result);
    }
}
