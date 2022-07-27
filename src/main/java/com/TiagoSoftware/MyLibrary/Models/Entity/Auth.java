package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Auth {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @Column(nullable = false, unique = false, length = 80)
    private String password;
}
