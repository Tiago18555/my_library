package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ClientUpdateDTO {

    @Size(min = 11, max = 16)
    @NotBlank
    public String cpf;

    @Size(min = 3, max = 40)
    public String name;
}
