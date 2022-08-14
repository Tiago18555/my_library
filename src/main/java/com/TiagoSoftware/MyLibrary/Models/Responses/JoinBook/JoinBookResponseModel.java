package com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinBookResponseModel {

    private Optional<UUID> id;
    private String title;
    private String authorName;
    private PublisherResponse publisher;
    private String description;
    private int availableAmount;
}