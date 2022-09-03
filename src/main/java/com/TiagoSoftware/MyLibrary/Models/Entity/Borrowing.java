package com.TiagoSoftware.MyLibrary.Models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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

    @ManyToOne
    public Client client;

    @OneToOne
    public Configuration configuration;
}

