package com.TiagoSoftware.MyLibrary.Repositories;

import com.TiagoSoftware.MyLibrary.Models.Entity.BookUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookUnitRepository  extends JpaRepository<BookUnit, Long> {
    List<BookUnit> findAllByBookId(UUID id);
}
