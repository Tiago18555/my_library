package com.TiagoSoftware.MyLibrary.Models.Responses.ListBooks;
import com.TiagoSoftware.MyLibrary.Models.Entity.Unit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBooksResponseModel {

    private Optional<UUID> id;
    private String title;
    private String authorName;
    private PublisherResponse publisher;
    private String description;
    private int availableAmount;

    @JsonIgnoreProperties("book")
    private List<Unit> bookUnits;
}