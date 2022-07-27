package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClientDTO {

    public String name;

    @NotBlank
    public String cpf;

    public Boolean isProfessor;

    @NotBlank
    public float loan;

    /*
    @NotBlank
    public List<Book> books;
    */
}
