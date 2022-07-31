package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class BookUpdateDTO{

    @Size(min = 3)
    @Size(max = 40)
    public String title;

    @NotNull
    public UUID author;

    @NotNull
    public UUID publisher;

    public String description;

    public int availableAmount;
}

