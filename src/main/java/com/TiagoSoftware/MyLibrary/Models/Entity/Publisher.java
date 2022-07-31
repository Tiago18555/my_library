package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;



import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "publisher")
@Data
public class Publisher {
    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false, unique = true, length = 40)
    public String name;

    @Column(nullable = false, unique = true, length = 40)
    public String cnpj;
}

