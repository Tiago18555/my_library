package com.TiagoSoftware.MyLibrary.Models.Responses.ListBooks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublisherResponse {
    private String publisherName;
    private String publisherCnpj;
}
