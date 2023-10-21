package com.example.cloudstorage.model;

import com.example.cloudstorage.security.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "password")
    private String password;
    @Column(name = "login")
    private String login;
    private String role;

    @ElementCollection
    private Set<Role> roles;

    @OneToMany
    @Column(name = "userFiles")
    private List<File> userFiles;

}
