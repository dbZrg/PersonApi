package com.db.rbazadatak.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String oib;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private Status status;
}