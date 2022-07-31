package com.TiagoSoftware.MyLibrary.Models.Responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Data
public class ResponseModel {

    public ResponseModel(Object data, HttpStatus httpstatus){
        this.data = data;
        this.httpstatus = httpstatus;
    }
    public ResponseModel(Object data, HttpStatus httpstatus, Optional<String> token){
        this.data = data;
        this.httpstatus = httpstatus;
        this.token = token;
    }

    private HttpStatus httpstatus;
    private Object data;
    private Optional<String> token;
}
