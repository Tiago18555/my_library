package com.TiagoSoftware.MyLibrary.Models.Responses.Client;

import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;


@Data
public class ClientResponse {

    public UUID id;

    public String name;

    public String cpf;

    public Boolean isProfessor;

    public double loan;

    @JsonIgnore()
    public List<Borrowing> borrowings;

    public Boolean isInactive;
}
