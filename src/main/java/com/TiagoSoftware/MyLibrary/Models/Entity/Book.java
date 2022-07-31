package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, length = 40, unique = true)
    public String title;

    @OneToOne
    public Author author;

    @OneToOne
    public Publisher publisher;

    public String description;

    public int availableAmount;
}

