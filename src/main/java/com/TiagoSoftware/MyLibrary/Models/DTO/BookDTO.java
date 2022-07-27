package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BookDTO {

    @NotBlank
    @Size(min = 3)
    @Size(max = 40)
    public String title;

    public Author authorId;

    public Publisher publisherId;
}
