package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorDTO;
import com.TiagoSoftware.MyLibrary.Services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<Object> RegisterNewUser(@RequestBody @Valid AuthorDTO authorDTO){
        var response = authorService.RegisterNewAuthor(authorDTO);

        return ResponseEntity
                .status(response.getHttpstatus())
                .body(response.getData());
    }
}
