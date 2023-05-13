package com.TiagoSoftware.MyLibrary.Models.Responses.ClientBorrowing;

import com.TiagoSoftware.MyLibrary.Models.Entity.Configuration;
import com.TiagoSoftware.MyLibrary.Models.Entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingResponseModel {

    public UUID id;

    public LocalDate startsAt;

    public LocalDate endsAt;

    public LocalDate deadLine;

    public Unit unit;

    public ClientResponse client;

    public Configuration configuration;
}
