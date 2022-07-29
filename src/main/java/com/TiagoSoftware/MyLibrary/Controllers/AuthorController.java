package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorDTO;
import com.TiagoSoftware.MyLibrary.Services.AuthorService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value="Registra um novo autor")
    public ResponseEntity registerAuthor(@RequestBody @Valid AuthorDTO authorDTO){
        var response = authorService.registerNewAuthor(authorDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value="Lista todos os autores cadastrados")
    public ResponseEntity listAuthors(){
        var response = authorService.listAuthors();

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
