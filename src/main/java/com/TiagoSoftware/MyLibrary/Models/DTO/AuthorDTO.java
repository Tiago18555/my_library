package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AuthorDTO {

    @NotBlank
    @Size(min = 3)
    @Size(max = 40)
    public String name;
}

