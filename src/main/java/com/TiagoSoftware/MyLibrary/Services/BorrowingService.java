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
    public ResponseModel UpdateBorrowLimitData(UUID id) {
        System.out.println("START HERE");
        try{
            var borrowings = dbset.findAllByClientId(id)
                    .stream()
                    .filter(x -> x.getEndsAt() == null)
                    .collect(Collectors.toList());

            System.out.println("Retrieve current configuration...");

            var foundConfig = configurationRepository.findAll();

            if (foundConfig.isEmpty()) {
                return new ResponseModel("There's no configuration on database, please create one first", HttpStatus.FORBIDDEN);
            }

            var lastConfig = foundConfig
                    .stream()
                    .skip(foundConfig.stream().count() - 1)
                    .limit(1)
                    .findFirst();

            if(borrowings.size() >= lastConfig.get().getBorrowingLimit()) {
                return new ResponseModel(new DataContainer(
                        "The limit of simultaneous borrowings has reached",
                        borrowings
                ), HttpStatus.OK);
            }

            return new ResponseModel(new DataContainer(
                    "The limit of simultaneous borrowings is " + lastConfig.get().getBorrowingLimit(),
                    borrowings
            ), HttpStatus.OK);

        }catch(Exception ex) {
            throw ex;
        }
    }
    @Transactional
    public ResponseModel UpdateLoanData(UUID id) {
        System.out.println("START HERE");
        try{
            double loan = 0d;
            var borrowings = dbset.findAllByClientId(id)
                    .stream()
                    .filter(x -> x.getEndsAt() == null)
                    .collect(Collectors.toList());
            var client = clientRepository.findById(id);
            if(client.isEmpty()) {
                return new ResponseModel("Student not found", HttpStatus.NOT_FOUND);
            }
            if(client.get().isProfessor.equals(true)){
                return new ResponseModel("This id belongs to a professor.", HttpStatus.BAD_REQUEST);
            }
            if(borrowings.isEmpty()) {
                return new ResponseModel(client, HttpStatus.OK);
            }
            for(Borrowing item : borrowings) {
                var configuration = configurationRepository.findById(item.getConfiguration().getId());
                System.out.println("item.getConfiguration().getId(): " + item.getConfiguration().getId());
                if(configuration.isEmpty()) {
                    return new ResponseModel("There's no configuration with this id", HttpStatus.NOT_FOUND);
                }
                var assessment = configuration.get().getAssessment();
                var tolerance = configuration.get().getTolerance();

                System.out.println("Assessment: " + assessment);
                System.out.println("Tolerance: " + tolerance);

                //Periodo de tempo em dias da data do empréstimo para a data atual, menos tempo de tolerância
                var borrowTime = Period.between(item.getStartsAt(), LocalDate.now()).getDays() - tolerance;

                System.out.println("Borrow time: " + borrowTime);

                if(borrowTime >= 1) {
                    loan += assessment * borrowTime;

                    System.out.println("Loan: " + loan);
                }
            }
            client.get().setLoan(loan);

            System.out.println("client.getloan: " + client.get().getLoan());
            var data = clientRepository.save(client.get());
            data.setBorrowings(
                    data.getBorrowings()
                            .stream()
                            .sorted(Comparator.comparing(x -> x.getDeadLine(), Collections.reverseOrder()))
                            .collect(Collectors.toList())
            );



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
        if(filter.get().equals("nextweek")) {
            var data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> x.getEndsAt() == null)
                    .filter(x -> Period.between(LocalDate.now(), x.getDeadLine()).getDays() <= 7)
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }

        return new ResponseModel("String query not found", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseModel DoBorrow(UUID id, BookUnitUpdateDTO bookDTO, Optional<Integer> debug) {

        System.out.println("Starting New Borrow...");
        System.out.println("Checking book, unit and client fields...");

        var foundUnit = bookUnitRepository.findById(bookDTO.getIbsn());
        var foundClient = clientRepository.findById(id);


        if(foundUnit.isEmpty() || foundUnit.get().getBorrowing() != null) {
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

        var client = new Client();
        BeanUtils.copyProperties(foundClient.get(), client);

        var foundBorrowing = dbset.findOpenBorrowingsByIbsn(unit.getIbsn());

        if(foundBorrowing.isPresent()) {
            return new ResponseModel("Error: There's a borrowing operation with this book(unit)", HttpStatus.NOT_FOUND);
        }

        System.out.println("Retrieve current configuration...");

        var foundConfig = configurationRepository.findAll();

        if (foundConfig.isEmpty()) {
            return new ResponseModel("There's no configuration on database, please create one first", HttpStatus.FORBIDDEN);
        }

        var lastConfig = foundConfig
                .stream()
                .skip(foundConfig.stream().count() - 1)
                .limit(1)
                .findFirst();

        System.out.println("Creating new borrowing register...");

        var borrowing = new Borrowing();

        if(debug.isPresent() && debug.get() > 0) {
            borrowing.setStartsAt(LocalDate.now().minusDays(debug.get()));
        } else {
            borrowing.setStartsAt(LocalDate.now());
        }

        borrowing.setDeadLine(borrowing.getStartsAt().plusDays(lastConfig.get().getTolerance()));
        borrowing.setUnit(foundUnit.get());
        borrowing.setClient(foundClient.get());
        borrowing.setConfiguration(lastConfig.get());

        System.out.println("Decrease one book on availableAmount, Adding client + book to each other");

        book.setAvailableAmount(book.getAvailableAmount() -1);

        System.out.println("Finish borrow operation");

        try {
            var Borrowing = dbset.save(borrowing);
            var Book = bookRepository.save(book);

            unit.setBorrowing(borrowing);

            System.out.println(">>>>>>>>>> FOI AQUI MERMO");
            client.borrowings.add(borrowing);

            var Unit = bookUnitRepository.save(unit);
            var Client = clientRepository.save(client);

            var data = new DataContainer(Borrowing, Book, Unit, Client);
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


        if(foundUnit.isEmpty() || foundUnit.get().getBorrowing() == null) {
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

        if (borrowing.isEmpty()) {
            return new ResponseModel("Borrowing null", HttpStatus.NOT_FOUND);
        }

        System.out.println("Devolution at: " + LocalDate.now());
        borrowing.get().setEndsAt(LocalDate.now());

        System.out.println("Increase one book on availableAmount");
        book.setAvailableAmount(book.getAvailableAmount() + 1);

        System.out.println("Removing borrowing operation from this unit");
        unit.setBorrowing(null);

        if(clearLoan.isPresent() && clearLoan.get().equals(true)) {
            //TODO: ADICIONAR PAGAMENTO PARCIAL DE MULTA DEPOIS [OU NÃO]
            client.setLoan(0);
        }

        System.out.println("Saving changes...");

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
