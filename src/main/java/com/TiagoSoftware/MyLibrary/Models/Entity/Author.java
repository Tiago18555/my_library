package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class Author {

    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, length = 40)
    public String name;
}

