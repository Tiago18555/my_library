package com.TiagoSoftware.MyLibrary.Models.Responses.ClientBorrowing;

import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
import com.TiagoSoftware.MyLibrary.Models.Entity.Configuration;
import com.TiagoSoftware.MyLibrary.Models.Entity.Unit;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BorrowingResponse {

    public UUID id;

    public LocalDate startsAt;

    public LocalDate endsAt;

    public LocalDate deadLine;

    public Unit unit;

    public ClientResponse client;

    public Configuration configuration;
}
