package com.s2i.sabong.service.dto;

import com.s2i.sabong.data.domain.AuthorityEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO {
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private boolean activated;
    private Set<AuthorityEntity> authorities = new HashSet<>();
}
