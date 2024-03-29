package com.TiagoSoftware.MyLibrary.Models.Responses.ClientBorrowing;

import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
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
