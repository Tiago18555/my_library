package com.TiagoSoftware.MyLibrary.Models.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataContainer {
    private Object primary;
    private Object secondary;
}

