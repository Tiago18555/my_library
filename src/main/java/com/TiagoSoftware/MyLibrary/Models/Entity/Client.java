package com.TiagoSoftware.MyLibrary.Models.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(nullable = false, length = 128)
    public String name;

    @Column(nullable = false, unique = true, length = 14)
    public String cpf;

    @Column(nullable = false)
    public Boolean isProfessor;

    @Column(nullable = true)
    public double loan;

    //@JsonManagedReference
    @OneToMany
    @JsonBackReference
    public List<Borrowing> borrowings;

    public Boolean isInactive;
}

