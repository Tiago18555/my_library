package com.TiagoSoftware.MyLibrary.Repositories;

import com.TiagoSoftware.MyLibrary.Models.Entity.Auth;

import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface AuthRepository extends JpaRepository<Auth, UUID> {

    public boolean existsByUsernameAndPassword(String username, String password);
    public boolean existsByUsername(String username);
    public Auth findByUsername(String username);
}
