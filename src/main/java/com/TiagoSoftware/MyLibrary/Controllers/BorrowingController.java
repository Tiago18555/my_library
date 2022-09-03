package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUnitUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ConfigurationDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Configuration;
import com.TiagoSoftware.MyLibrary.Services.BorrowingService;
import com.TiagoSoftware.MyLibrary.Services.ConfigurationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/borrowing")
public class BorrowingController {

    @Autowired
    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("{id}")
    @ApiOperation(value = "Realiza o empréstimo")
    public ResponseEntity DoBorrow(@PathVariable UUID id, @RequestBody BookUnitUpdateDTO book, @RequestParam Optional<Integer> debugDelay) {
        var response = borrowingService.DoBorrow(id, book, debugDelay);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Verifica se o o limite de empréstimos atingiu o limite ou se possui multa pendente")
    public ResponseEntity VerifyLoanStatus(@PathVariable UUID id) {
        var response = borrowingService.UpdateLoanData(id);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PatchMapping("{id}")
    @ApiOperation(value = "Realiza a devolução")
    public ResponseEntity DoDevolution(@PathVariable UUID id, @RequestBody BookUnitUpdateDTO book, @RequestParam Optional<Boolean> cleanLoan) {
        var response = borrowingService.DoDevolution(id, book, cleanLoan);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value = "Lista todos os empréstimos")
    public ResponseEntity ListAllBorrowings(@RequestParam Optional<String> filter) {
        var response = borrowingService.ListAllBorrowings(filter);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}

