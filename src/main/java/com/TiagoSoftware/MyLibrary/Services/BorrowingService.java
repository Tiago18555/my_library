package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookUnitUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.*;
import com.TiagoSoftware.MyLibrary.Models.Responses.DataContainer;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
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

    @Transactional
    public ResponseModel UpdateLoanData(UUID id) {
        try{
            var borrowings = dbset.findAllByClientId(id);
            double loan = 0d;
            //List<Configuration> configurations = new ArrayList<>();
            var client = clientRepository.findById(id);
            if(client.isEmpty()) {
                return new ResponseModel("Student not found", HttpStatus.NOT_FOUND);
            }
            for(Borrowing item : borrowings) {
                var configuration = configurationRepository.findById(item.getConfiguration().getId());
                if(configuration.isEmpty()) {
                    return new ResponseModel("There's no configuration with this id", HttpStatus.NOT_FOUND);
                }
                var assessment = configuration.get().getAssessment();
                var tolerance = configuration.get().getTolerance();

                //Periodo de tempo em dias da data do empréstimo para a data atual, menos tempo de tolerância
                var borrowTime = Period.between(item.getStartsAt(), LocalDate.now()).getDays() - tolerance;

                if(borrowTime >= 1) {
                    loan += assessment * borrowTime;
                }
            }
            client.get().setLoan(loan);
            var data = clientRepository.save(client.get());

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
    public ResponseModel ListAllBorrowings(Optional<String> filter) {
        if(filter.isEmpty()) {
            var data = dbset.findAll();
            return new ResponseModel(data, HttpStatus.OK);
        }

        if(filter.get().equals("student")) {
            var data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> !x.getClient().getIsProfessor())
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }
        if(filter.get().equals("professor")) {
            var data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> x.getClient().getIsProfessor())
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }
        if(filter.get().equals("unfinished")) {
            var data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> x.getEndsAt() == null)
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }
        if(filter.get().equals("finished")) {
            var data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> x.getEndsAt() != null)
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }

        return new ResponseModel("String query not found", HttpStatus.BAD_REQUEST);
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

        if(foundBook.isEmpty() || foundBook.get().getAvailableAmount() < 1) {
            return new ResponseModel("Book not found or unavailable", HttpStatus.NOT_FOUND);
        }

        System.out.println("Mapping found entity to a class repository to apply changes...");

        var unit = new Unit();
        BeanUtils.copyProperties(foundUnit.get(), unit);

        var book = new Book();
        BeanUtils.copyProperties(foundBook.get(), book);

        var foundBorrowing = dbset.findOpenBorrowingsByIbsn(unit.getIbsn());

        if(foundBorrowing.isPresent()) {
            return new ResponseModel("Error: There's a borrowing operation with this book(unit)", HttpStatus.NOT_FOUND);
        }

        System.out.println("Retrieve current configuration...");

        var foundConfig = configurationRepository.findAll();
        var lastConfig = foundConfig
                .stream()
                .skip(foundConfig.stream().count() - 1)
                .limit(1)
                .findFirst();

        if (lastConfig.isEmpty()) {
            return new ResponseModel("There's no configuration on database, please create one first", HttpStatus.FORBIDDEN);
        }

        System.out.println("Creating new borrowing register...");

        var borrowing = new Borrowing();
        borrowing.setStartsAt(LocalDate.now());
        borrowing.setDeadLine(LocalDate.now().plusDays(lastConfig.get().getTolerance()));
        borrowing.setUnit(foundUnit.get());
        borrowing.setClient(foundClient.get());
        borrowing.setConfiguration(lastConfig.get());

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

    @Transactional
    public ResponseModel DoDevolution(UUID id, BookUnitUpdateDTO bookDTO, Optional<Boolean> clearLoan) {
        System.out.println("Starting New Devolution...");
        System.out.println("Checking book, unit and client fields...");

        var foundUnit = bookUnitRepository.findById(bookDTO.getIbsn());
        var foundClient = clientRepository.findById(id);

        if(foundUnit.isEmpty() || foundUnit.get().getClient() == null) {
            return new ResponseModel("Unit not found or unavailable for devolution.", HttpStatus.NOT_FOUND);
        }

        if(foundClient.isEmpty()) {
            return new ResponseModel("User not found.", HttpStatus.NOT_FOUND);
        }

        var foundBook = bookRepository.findById(foundUnit.get().getBook().getId());

        if(foundBook.isEmpty()) {
            return new ResponseModel("Book not found", HttpStatus.NOT_FOUND);
        }

        System.out.println("Mapping found entity to a class repository to apply changes...");

        var unit = new Unit();
        BeanUtils.copyProperties(foundUnit.get(), unit);

        var book = new Book();
        BeanUtils.copyProperties(foundBook.get(), book);

        var client = new Client();
        BeanUtils.copyProperties(foundClient.get(), client);

        System.out.println("Finalizing borrow register date");

        var borrowing = dbset.findOpenBorrowingsByIbsn(unit.getIbsn());
        //var borrowing = dbset.findAllByUnitIbsn(unit.getIbsn());
        /*
        if(!borrowing.isEmpty()) {
            borrowing
                    .stream()
                    .filter(x -> x.getEndsAt() == null)
                    .collect(Collectors.toList());

            if ( (long) borrowing.size() < 2 ) {
                return new ResponseModel("Borrowing null", HttpStatus.NOT_FOUND);
            }
        }
        */

        if (borrowing.isEmpty()) {
            return new ResponseModel("Borrowing null", HttpStatus.NOT_FOUND);
        }


        System.out.println(borrowing.get().getId());
        borrowing.get().setEndsAt(LocalDate.now());

        System.out.println("Increase one book on availableAmount...\nCleaning client register on this unit...\nRemoving bookUnit from Client");

        List<Unit> books = client.getBooks();
        books.remove(unit);

        book.setAvailableAmount(book.getAvailableAmount() + 1);
        unit.setClient(null);

        if(clearLoan.isPresent() && clearLoan.get().equals(true)) {
            //TODO: ADICIONAR PAGAMENTO PARCIAL DE MULTA DEPOIS
            client.setLoan(0);
        }

        System.out.println("Finish borrow operation");

        try{
            var first = dbset.save(borrowing.get());
            var second = bookRepository.save(book);
            var third = bookUnitRepository.save(unit);
            var fourth = clientRepository.save(client);
            var data = new DataContainer(first, second, third, fourth);
            return new ResponseModel(data, HttpStatus.CREATED);
        }
        catch (Exception ex) {
            throw ex;
        }
    }
}
