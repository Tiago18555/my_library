package com.TiagoSoftware.MyLibrary.Models.Entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "configuration")
public class Configuration {

    @Id
    @GeneratedValue
    public UUID id;

    public Date startedAt;

    @Min(1)
    @Max(365)
    public Integer tolerance;

    @Max(100)
    public Double assessment;
}
