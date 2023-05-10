package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class BookUpdateDTO {

    @NotNull
    public UUID id;

    @Size(min = 3)
    @Size(max = 40)
    public String title;

    public UUID author;

    public UUID publisher;

    public String description;
}

