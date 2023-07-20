package com.db.rbazadatak.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.management.relation.Role;

@Entity
@Table(name = "API_USER")
@Data
public class ApiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role;

    public ApiUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ApiUser() {
    }
}
