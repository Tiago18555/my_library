package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;



import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, length = 40)
    public String name;

    @Column(nullable = false, unique = true, length = 14)
    public String cpf;

    @Column(nullable = false)
    public Boolean isProfessor;

    @Column(nullable = true)
    public float loan;

    @OneToMany
    public List<BookUnit> books;

    @OneToMany
    public List<Borrowing> borrowings;
    public Boolean isInactive;
}

