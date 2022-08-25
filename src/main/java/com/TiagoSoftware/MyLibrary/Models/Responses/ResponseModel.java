package com.TiagoSoftware.MyLibrary.Models.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Data
public class ResponseModel {

    public ResponseModel(Object data, HttpStatus httpstatus) {
        this.data = data;
        this.httpstatus = httpstatus;
    }
    public ResponseModel(Object data, HttpStatus httpstatus, Optional<String> token) {
        this.data = data;
        this.httpstatus = httpstatus;
        this.token = token;
    }

    private HttpStatus httpstatus;
    private Object data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Optional<String> token;
}
