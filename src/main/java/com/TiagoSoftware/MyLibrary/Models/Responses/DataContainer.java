package com.TiagoSoftware.MyLibrary.Models.Responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.ConstructorParameters;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataContainer {

    public DataContainer(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    public DataContainer(Object first, Object second, Object third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object first;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object second;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object third;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object fourth;
}

