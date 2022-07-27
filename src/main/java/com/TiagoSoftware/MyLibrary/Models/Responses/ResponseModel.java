package com.TiagoSoftware.MyLibrary.Models.Responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseModel {

    public ResponseModel(Object data, HttpStatus httpstatus){
        this.data = data;
        this.httpstatus = httpstatus;
    }

    private HttpStatus httpstatus;
    private Object data;
}
