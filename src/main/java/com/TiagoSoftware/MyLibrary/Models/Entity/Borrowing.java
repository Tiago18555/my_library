package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "borrowing")
public class Borrowing {

    @Id
    @GeneratedValue
    public UUID id;

    public Date startsAt;

    public Date endsAt;

    @OneToOne
    public Book book;

    @ManyToOne
    public Client client;
}
