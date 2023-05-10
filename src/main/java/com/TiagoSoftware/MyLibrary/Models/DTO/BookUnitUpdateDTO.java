package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
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
public class BookUnitUpdateDTO {

    @NotNull
    public long ibsn;

    @Size(min = 3, max = 128)
    public String title;
}

