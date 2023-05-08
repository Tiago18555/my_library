package com.TiagoSoftware.MyLibrary.Models.DTO;

import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class ClientUpdateDTO {

    @Size(min = 14, max = 16)
    @NotBlank
    public String cpf;

    @Size(min = 3, max = 40)
    public String name;

    public float loan;

    public List<Borrowing> borrowings;

}
