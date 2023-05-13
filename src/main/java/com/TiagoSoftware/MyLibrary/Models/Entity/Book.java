package com.TiagoSoftware.MyLibrary.Models.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, length = 128, unique = true)
    public String title;

    @OneToOne
    public Author author;

    @OneToOne
    public Publisher publisher;

    @Column(length = 2048)
    public String description;

    public int availableAmount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    public List<Unit> units;
}

