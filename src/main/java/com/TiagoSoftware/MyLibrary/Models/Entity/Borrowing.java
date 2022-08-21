package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "borrowing")
public class Borrowing {

    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false)
    public Date startsAt;

    public Date endsAt;

    public LocalDate deadLine;

    @OneToOne
    public BookUnit book;

    @ManyToOne
    public Client client;

    @OneToOne
    public Configuration configuration;
}

