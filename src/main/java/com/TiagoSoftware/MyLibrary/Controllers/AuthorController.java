package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Services.AuthorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    @ApiOperation(value="Registra um novo autor")
    public ResponseEntity registerAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        var response = authorService.registerNewAuthor(authorDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value="Lista todos os autores cadastrados")
    public ResponseEntity listAuthors() {
        var response = authorService.listAuthors();

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclui um(a) autor(a) da base de dados")
    public ResponseEntity deleteAuthor(@PathVariable UUID id) {
        var response = authorService.deleteAuthorById(id);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PutMapping
    @ApiOperation(value="Altera um registro de um(a) autor(a) cadastrado(a)")
    public ResponseEntity editAuthor(@RequestBody @Valid AuthorUpdateDTO authorUpdateDTO) {
        var response = authorService.editAuthor(authorUpdateDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
