package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.BorrowingRepository;
import com.TiagoSoftware.MyLibrary.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Service
public class BorrowingService {
    @Autowired
    private final BorrowingRepository dbset;

    @Autowired
    private final ClientRepository clientRepo;

    public BorrowingService(BorrowingRepository dbset, ClientRepository clientRepo) {
        this.dbset = dbset;
        this.clientRepo = clientRepo;
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

    public ResponseModel DoBorrow(UUID id, BookUpdateDTO bookDTO, Integer deadLine) {
        var borrowing = new Borrowing();
        var book = new Book();
        book.setId(bookDTO.getId());
        var client = new Client();
        client.setId(id);


        borrowing.setStartsAt(new Date(System.currentTimeMillis()));
        borrowing.setDeadLine(LocalDate.now().plusDays(deadLine));
        borrowing.setBook(book);
        borrowing.setClient(client);

        try{
            return new ResponseModel(dbset.save(borrowing), HttpStatus.CREATED);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
