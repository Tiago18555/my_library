package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class BookDTO {

    @NotBlank
    @Size(min = 3, max = 40)
    public String title;

    @NotNull
    public UUID author;

    @NotNull
    public UUID publisher;

    @Size(min = 10, max = 2048)
    public String description;

    public int availableAmount = 1;
}

