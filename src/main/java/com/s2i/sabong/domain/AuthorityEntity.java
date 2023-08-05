package com.s2i.sabong.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "authority", schema = "sabong")
public class AuthorityEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    @Size(max = 250)
    @Column(length = 250)
    private String description;

}
