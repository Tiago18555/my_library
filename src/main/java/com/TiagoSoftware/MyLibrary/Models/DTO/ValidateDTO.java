package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class ValidateDTO {

    @NotBlank
    private String token;
}

