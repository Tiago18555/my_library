package com.TiagoSoftware.MyLibrary.Repositories;

import com.TiagoSoftware.MyLibrary.Models.Entity.BookUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookUnitRepository  extends JpaRepository<BookUnit, UUID> {
}
