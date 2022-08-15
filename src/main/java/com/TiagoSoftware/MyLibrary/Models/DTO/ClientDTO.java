package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class ClientDTO {

    @Size(min = 3)
    @Size(max = 40)
    public String name;

    @Size(min = 14)
    @Size(max = 16)
    @NotBlank
    public String cpf;

    public float loan;

    public List<Book> books;

}
