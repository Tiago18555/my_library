package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "author")
@Data
public class Author {

    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, length = 128, unique = true)
    public String name;
}

