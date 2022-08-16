package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PublisherUpdateDTO {

    @Size(min = 3)
    @Size(max = 40)
    public String name;

    @Size(max = 18)
    @NotNull
    public String cnpj;
}
