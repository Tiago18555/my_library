package com.TiagoSoftware.MyLibrary.Repositories;

import com.TiagoSoftware.MyLibrary.Models.Entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConfigurationRepository extends JpaRepository<Configuration, UUID>  {
}
