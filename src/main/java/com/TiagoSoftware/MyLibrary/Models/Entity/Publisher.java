package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Publisher {
    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, unique = true, length = 40)
    public String name;

    @Column(nullable = false, unique = true, length = 40)
    public String cnpj;
}

