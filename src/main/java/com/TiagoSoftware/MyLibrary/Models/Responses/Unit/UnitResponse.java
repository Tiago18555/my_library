package com.TiagoSoftware.MyLibrary.Models.Responses.Unit;

import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitResponse {

    @JsonInclude
    public Long ibsn;

    @JsonIgnore
    public Book book;

    @JsonIgnore
    public Borrowing borrowing;
}
