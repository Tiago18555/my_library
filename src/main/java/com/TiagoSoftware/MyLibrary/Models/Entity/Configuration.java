package com.TiagoSoftware.MyLibrary.Models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    //@JsonIgnore
    public UUID id;

    public Date startedAt;

    @Min(1)
    @Max(365)
    public Integer tolerance;

    @Max(100)
    public Double assessment;

    @Min(1)
    @Max(50)
    public Integer borrowingLimit;
}


