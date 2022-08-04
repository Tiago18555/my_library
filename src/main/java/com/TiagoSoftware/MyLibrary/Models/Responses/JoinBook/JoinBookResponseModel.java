package com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinBookResponseModel {

    private String title;
    private String authorName;
    private PublisherResponse publisher;
    private String description;
    private int availableAmount;
}