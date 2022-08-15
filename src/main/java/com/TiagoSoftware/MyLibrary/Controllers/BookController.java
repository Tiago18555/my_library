package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Services.AuthorService;
import com.TiagoSoftware.MyLibrary.Services.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/register")
    @ApiOperation(value="Registra um novo livro")
    public ResponseEntity registerBook(@RequestBody @Valid BookDTO bookDTO) {
        var response = bookService.registerNewBook(bookDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value="Lista todos os livros cadastrados")
    public ResponseEntity listBooks(@RequestParam Optional<String> author, @RequestParam Optional<String> publisher) {
        var response = bookService.listBooks(author, publisher);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PutMapping
    @ApiOperation(value="Altera um registro de livro cadastrado")
    public ResponseEntity editBook(@RequestBody @Valid BookUpdateDTO bookDTO) {
        var response = bookService.editBook(bookDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("/{title}")
    @ApiOperation(value="Exibe as informações detalhadas de um livro")
    public ResponseEntity getBookById(@PathVariable String title) {
        var response = bookService.getBookByTitle(title);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclui um livro da base de dados")
    public ResponseEntity deleteBook(@PathVariable UUID id) {
        var response = bookService.deleteBookById(id);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
