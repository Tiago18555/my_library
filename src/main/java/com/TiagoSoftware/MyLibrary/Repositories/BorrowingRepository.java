package com.TiagoSoftware.MyLibrary.Repositories;

import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BorrowingRepository extends JpaRepository<Borrowing, UUID> {

    List<Borrowing> findAllByClientId(UUID client);
    @Query( value = "SELECT b FROM Borrowing b JOIN b.unit u WHERE u.ibsn = :ibsn AND b.endsAt IS NULL" )
    Optional<Borrowing> findOpenBorrowingsByIbsn(@Param("ibsn") Long ibsn);
}
