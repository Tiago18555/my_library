package com.TiagoSoftware.MyLibrary.Repositories;

import com.TiagoSoftware.MyLibrary.Models.Entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookUnitRepository  extends JpaRepository<Unit, Long> {
    List<Unit> findAllByBookId(UUID id);
    Optional<Unit> findByIbsn(Long ibsn);
}
