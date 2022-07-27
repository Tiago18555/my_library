package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PublisherDTO {

    @NotBlank
    @Size(min = 3)
    @Size(max = 40)
    public String name;

    @NotBlank
    @Size(max = 14)
    public String cnpj;
}
