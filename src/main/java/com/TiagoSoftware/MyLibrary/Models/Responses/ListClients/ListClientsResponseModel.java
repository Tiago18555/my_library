package com.TiagoSoftware.MyLibrary.Models.Responses.ListClients;

import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListClientsResponseModel {

    public UUID id;

    public String name;

    public String cpf;

    @JsonIgnore
    public Boolean isProfessor;

    public double loan;

    @JsonIgnore
    public List<Borrowing> borrowings;

    @JsonIgnore
    public Boolean isInactive;
}
