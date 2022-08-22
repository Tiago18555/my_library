package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookUnitUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.BookUnit;
import com.TiagoSoftware.MyLibrary.Models.Entity.Borrowing;
import com.TiagoSoftware.MyLibrary.Models.Responses.DataContainer;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private final ConfigurationRepository configurationRepository;

    public BorrowingService(BorrowingRepository dbset, ClientRepository clientRepo, BookRepository bookRepository, BookUnitRepository bookUnitRepository, ConfigurationRepository configurationRepository) {
        this.dbset = dbset;
        this.clientRepository = clientRepo;
        this.bookRepository = bookRepository;
        this.bookUnitRepository = bookUnitRepository;
        this.configurationRepository = configurationRepository;
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
    public ResponseModel DoBorrow(UUID id, BookUnitUpdateDTO bookDTO) {

        System.out.println("Starting New Borrow...");
        System.out.println("Checking book, unit and client fields...");

        var foundUnit = bookUnitRepository.findById(bookDTO.getIbsn());
        var foundClient = clientRepository.findById(id);

        if(foundUnit.isEmpty() || foundUnit.get().getClient() != null) {
            return new ResponseModel("Unit not found or unavailable.", HttpStatus.NOT_FOUND);
        }

        if(foundClient.isEmpty()) {
            return new ResponseModel("User not found.", HttpStatus.NOT_FOUND);
        }

        var foundBook = bookRepository.findById(foundUnit.get().getBook().getId());

        if(foundBook.isEmpty()) {
            return new ResponseModel("Book not found", HttpStatus.NOT_FOUND);
        }

        System.out.println("Mapping found entity to a class repository to apply changes...");

        var unit = new BookUnit();
        BeanUtils.copyProperties(foundUnit.get(), unit);

        var book = new Book();
        BeanUtils.copyProperties(foundBook.get(), book);

        System.out.println("Retrieve current configuration...");

        var foundConfig = configurationRepository.findAll();
        var lastConfig = foundConfig.stream().skip(foundConfig.stream().count() - 1).limit(1).collect(Collectors.toList());

        if (lastConfig.isEmpty() || lastConfig.get(0) == null) {
            return new ResponseModel("There's no configuration on database, please create one first", HttpStatus.FORBIDDEN);
        }

        System.out.println("Creating new borrowing register...");

        var borrowing = new Borrowing();
        borrowing.setStartsAt(new Date(System.currentTimeMillis()));
        borrowing.setDeadLine(LocalDate.now().plusDays(lastConfig.get(0).getTolerance()));
        borrowing.setBook(foundUnit.get());
        borrowing.setClient(foundClient.get());

        System.out.println("Decrease one book on availableAmount, Adding client + book to each other");

        book.setAvailableAmount(book.getAvailableAmount() -1);
        unit.setClient(foundClient.get());

        System.out.println("Finish borrow operation");

        try{
            var first = dbset.save(borrowing);
            var second = bookRepository.save(book);
            var third = bookUnitRepository.save(unit);
            var data = new DataContainer(first, second, third);
            return new ResponseModel(data, HttpStatus.CREATED);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

}
