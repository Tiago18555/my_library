package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingDTO {

    @NotNull
    public Long unit;

    @NotNull
    public UUID client;

    @OneToOne
    public UUID configuration;
}
