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

    @PostMapping("{cpf}")
    @ApiOperation(value = "Realiza o empréstimo")
    public ResponseEntity DoBorrow(@PathVariable String cpf, @RequestBody BookUnitUpdateDTO book, @RequestParam Optional<Integer> debugDelay) {
        var response = borrowingService.DoBorrow(cpf, book, debugDelay);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Verifica se o o limite de empréstimos atingiu o limite ou se possui multa pendente")
    public ResponseEntity VerifyLoanStatus(@PathVariable UUID id) {
        var response = borrowingService.UpdateLoanData(id);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("professor/{id}")
    @ApiOperation(value = "Verifica se o limite de empréstimos simultâneos foi atingido")
    public ResponseEntity UpdateBorrowLimitData(@PathVariable UUID id) {
        var response = borrowingService.UpdateBorrowLimitData(id);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PatchMapping("{cpf}")
    @ApiOperation(value = "Realiza a devolução")
    public ResponseEntity DoDevolution(@PathVariable String cpf, @RequestBody BookUnitUpdateDTO book, @RequestParam Optional<Boolean> cleanLoan) {
        var response = borrowingService.DoDevolution(cpf, book, cleanLoan);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value = "Lista todos os empréstimos", produces = "O Limite de resultados para empréstimos finalizados é 500.")
    public ResponseEntity ListAllBorrowings(@RequestParam Optional<String> filter) {
        var response = borrowingService.ListAllBorrowings(filter);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}

