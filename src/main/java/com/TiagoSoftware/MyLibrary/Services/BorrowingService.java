package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.BookRepository;
import com.TiagoSoftware.MyLibrary.Repositories.BookUnitRepository;
import com.TiagoSoftware.MyLibrary.Repositories.BorrowingRepository;
import com.TiagoSoftware.MyLibrary.Repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BorrowingService {
    @Autowired
    private final BorrowingRepository dbset;

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final BookUnitRepository bookUnitRepository;

    public BorrowingService(BorrowingRepository dbset, ClientRepository clientRepo, BookRepository bookRepository, BookUnitRepository bookUnitRepository) {
        this.dbset = dbset;
        this.clientRepository = clientRepo;
        this.bookRepository = bookRepository;
        this.bookUnitRepository = bookUnitRepository;
    }

    public ResponseModel UpdateLoanData(UUID id) {
        try{
            var data = dbset.findAllByClientId(id);
            return new ResponseModel(
                    data,
                    HttpStatus.OK
            );
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional
    public ResponseModel DoBorrow(UUID id, BookUpdateDTO bookDTO, Integer deadLine) {
        return new ResponseModel("", HttpStatus.ACCEPTED);

        /*
        System.out.println("Starting New Borrow...");

        var foundBook = bookUnitRepository.findById(bookDTO.getId());
        var foundClient = clientRepository.findById(id);

        if(foundBook.isEmpty()) {
            return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
        }

        if(foundClient.isEmpty()) {
            return new ResponseModel("User not found.", HttpStatus.NOT_FOUND);
        }

        var book = new Book();
        BeanUtils.copyProperties(foundBook.get(), book);


        System.out.println("Creating new borrowing register...");

        var borrowing = new Borrowing();
        borrowing.setStartsAt(new Date(System.currentTimeMillis()));
        borrowing.setDeadLine(LocalDate.now().plusDays(deadLine));
        borrowing.setBook(foundBook.get().getUnits());
        borrowing.setClient(foundClient.get());

        System.out.println("Decrease one book on availableAmount, Adding client + book to each other");

        List<Client> clients = new ArrayList<>();
        clients.addAll(foundBook.get().getClients());
        clients.add(foundClient.get());
        book.setAvailableAmount(book.getAvailableAmount() -1);
        book.setClients(clients);
        bookRepository.save(book);

        System.out.println("Finish borrow operation");

        try{
            return new ResponseModel(dbset.save(borrowing), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            throw ex;
        }

         */
    }

}
