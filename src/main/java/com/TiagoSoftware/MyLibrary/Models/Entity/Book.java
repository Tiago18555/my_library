package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, length = 40)
    public String title;

    @OneToOne//(targetEntity=Entity.Author)
    public Author authorId;

    @OneToOne//(targetEntity=Entity.Author)
    public Publisher publisherId;
}

