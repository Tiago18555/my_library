package com.TiagoSoftware.MyLibrary.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationDTO {

    @Min(1)
    @Max(365)
    public Integer tolerance;

    @Max(100)
    public Double assessment;

    @Min(1)
    @Max(50)
    public Integer borrowingLimit;
}
