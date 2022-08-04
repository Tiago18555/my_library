package com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PublisherResponse {
    private String publisherName;
    private String publisherCnpj;
}
