package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class BookChangeAmountDTO {

    @NotNull
    @NotBlank
    public String title;

    @NotNull
    public int availableAmount;
}
