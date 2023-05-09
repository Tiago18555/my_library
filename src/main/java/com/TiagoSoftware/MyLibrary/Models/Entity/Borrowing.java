package com.TiagoSoftware.MyLibrary.Models.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "borrowing")
public class Borrowing {

    @Id
    @GeneratedValue
    //@JsonIgnore
    public UUID id;

    @Column(nullable = false)
    public LocalDate startsAt;

    public LocalDate endsAt;

    public LocalDate deadLine;

    @OneToOne
    public Unit unit;

    //@JsonBackReference
    @ManyToOne
    @JsonManagedReference
    public Client client;

    @OneToOne
    public Configuration configuration;
}