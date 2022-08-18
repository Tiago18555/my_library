package com.TiagoSoftware.MyLibrary.Repositories;

import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface BorrowingRepository extends JpaRepository<Borrowing, UUID> {

    List<Borrowing> findAllByClientId(UUID client);
}
