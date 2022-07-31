package com.TiagoSoftware.MyLibrary.Models.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinBookResponseModel {

    private String title;
    private String description;
    private int availableAmount;
    private String authorName;
    private String publisherName;
    private String publisherCnpj;
}