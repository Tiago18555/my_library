package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "book_unit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit {
    @Id
    @GeneratedValue
    public Long ibsn;

    @ManyToOne
    public Book book;

    @ManyToOne
    public Client client;
}