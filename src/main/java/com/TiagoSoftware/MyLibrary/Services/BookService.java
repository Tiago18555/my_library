package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.*;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Unit;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import com.TiagoSoftware.MyLibrary.Models.Responses.DataContainer;
import com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook.JoinBookResponseModel;
import com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook.PublisherResponse;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Models.Responses.Unit.UnitResponse;
import com.TiagoSoftware.MyLibrary.Repositories.BookRepository;
import com.TiagoSoftware.MyLibrary.Repositories.BookUnitRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private final BookRepository dbset;

    @Autowired
    private final BookUnitRepository unitRepo;

    public BookService(BookRepository dbset, BookUnitRepository unitRepo) {
        this.dbset = dbset;
        this.unitRepo = unitRepo;
    }

    @Transactional
    public ResponseModel registerNewBook(BookDTO bookDTO) {
        var book = new Book();

        // MANUAL COPY PROPERTIES
        System.out.println("AUTHOR: " + bookDTO.getAuthor() + "\nPUB: " + bookDTO.getPublisher());


        Author author = new Author();
        author.setId(bookDTO.getAuthor());
        Publisher publisher = new Publisher();
        publisher.setId(bookDTO.getPublisher());

        book.setTitle(bookDTO.getTitle());

        book.setAuthor(author);
        book.setPublisher(publisher);

        book.setDescription(bookDTO.getDescription() != null ? bookDTO.getDescription() : null);
        book.setAvailableAmount(bookDTO.getAvailableAmount() != 0 ? bookDTO.getAvailableAmount() : 1);



        try {
            if (dbset.findByTitle(book.getTitle()).isPresent()) {
                return new ResponseModel("This books is already exists. Did you meant to change his quantity?", HttpStatus.FORBIDDEN);
            }
            var primary = dbset.save(book);

            for(int i = 0; i < book.getAvailableAmount(); i++) {
                var unitDTO = new BookUnitDTO(book);
                var unit = new Unit();
                BeanUtils.copyProperties(unitDTO, unit);
                System.out.println(unit.getIbsn());
                unitRepo.save(unit);
            }
            var response = unitRepo.findAllByBookId(book.getId());
            var secondary = this.mappingUnits(response);
            var data = new DataContainer(primary, secondary);
            return new ResponseModel(data, HttpStatus.CREATED);
        }
        catch(Exception ex){
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseModel listBooks(Optional<String> author, Optional<String> publisher, Optional<Boolean> onlyAvailable){
        try{
            if(author.isPresent() && publisher.isPresent()) {
                return new ResponseModel("Use just one search filter per request", HttpStatus.BAD_REQUEST);
            }

            List<Book> books = new ArrayList<>();

            if(author.isPresent() && publisher.isEmpty() && onlyAvailable.isEmpty()) {
                books = dbset
                    .findAllCompletly()
                    .stream()
                    .filter( x -> x.getAuthor().getName().equals ( author.get() ) )
                    .collect(Collectors.toList());
            }

            if(publisher.isPresent() && author.isEmpty() && onlyAvailable.isEmpty()) {
                books = dbset
                    .findAllCompletly()
                    .stream()
                    .filter( x -> x.getPublisher().getName().equals ( publisher.get() ) )
                    .collect(Collectors.toList());
            }

            if(publisher.isEmpty() && author.isEmpty()) {
                if(onlyAvailable.isPresent() && onlyAvailable.get().equals(true) ) {
                    books = dbset
                        .findAllCompletly()
                        .stream()
                        .filter( x -> x.getAvailableAmount() > 1)
                        .collect(Collectors.toList());
                } else {
                    books = dbset.findAllCompletly();
                }
            }
/*
            List<JoinBookResponseModel> data = books.stream().map(book -> {
                JoinBookResponseModel jbrm = new JoinBookResponseModel();

                jbrm.setId(Optional.of(book.getId()));
                jbrm.setTitle(book.getTitle());
                jbrm.setAuthorName(book.getAuthor().getName());
                jbrm.setPublisher( new PublisherResponse(
                        book.getPublisher().getName(),
                        book.getPublisher().getCnpj()
                ));
                jbrm.setDescription(book.getDescription());
                jbrm.setAvailableAmount(book.getAvailableAmount());
                jbrm.setBookUnits(book.getUnits());

                return jbrm;
            }).collect(Collectors.toList());*/

            return new ResponseModel(books, HttpStatus.OK);
        }
        catch(Exception ex){
            throw ex;
        }
    }

    @Transactional
    public ResponseModel editBook(BookUpdateDTO bookDTO) {
        var book = new Book();

        Author author = new Author();
        author.setId(bookDTO.getAuthor());
        Publisher publisher = new Publisher();
        publisher.setId(bookDTO.getPublisher());

        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle() != null ? bookDTO.getTitle() : null);
        book.setAuthor(author.getId() != null ? author : null);
        book.setPublisher(publisher.getId() != null ? publisher : null);
        book.setDescription(bookDTO.getDescription() != null ? bookDTO.getDescription() : null);

        try {
            Optional<Book> foundBook = dbset.findById(book.getId());
            if(foundBook.isEmpty()) {
                return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
            }

            book.setTitle(book.getTitle() != null ? book.getTitle() : foundBook.get().getTitle());
            book.setAvailableAmount(foundBook.get().getAvailableAmount());
            book.setAuthor(book.getAuthor() != null ? book.getAuthor() : foundBook.get().getAuthor());
            book.setPublisher(book.getPublisher() != null ? book.getPublisher() : foundBook.get().getPublisher());
            book.setDescription(book.getDescription() != null ? book.getDescription() : foundBook.get().getDescription());

            return new ResponseModel(dbset.save(book), HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Transactional
    public ResponseModel addNewUnits(BookChangeAmountDTO bookDTO) {
        var book = new Book();
        var foundBook = dbset.findByTitle(bookDTO.getTitle());
        if (foundBook.isEmpty()) {
            return new ResponseModel("Book not found", HttpStatus.NOT_FOUND);
        }
        var newAmount = bookDTO.getAvailableAmount() - foundBook.get().getAvailableAmount();
        if (newAmount <= 0) {
            return new ResponseModel("New quantity needs to be more than the current one", HttpStatus.BAD_REQUEST);
        }

        try{
            BeanUtils.copyProperties(foundBook.get(), book);
            book.setAvailableAmount(bookDTO.getAvailableAmount());
            dbset.save(book);

            var primary = dbset.save(book);

            for(int i = 0; i < newAmount; i++) {
                var unitDTO = new BookUnitDTO(book);
                var unit = new Unit();
                BeanUtils.copyProperties(unitDTO, unit);
                System.out.println(unit.getIbsn());
                unitRepo.save(unit);
            }
            var secondary = unitRepo.findAllByBookId(book.getId());
            var data = new DataContainer(primary, secondary);
            return new ResponseModel(data, HttpStatus.CREATED);
        }
        catch(Exception ex){
            throw ex;
        }
    }

    @Transactional
    public ResponseModel deleteUnit(BookUnitUpdateDTO bookUnitDTO) {

        if(bookUnitDTO.getIbsn() == 0){
            return new ResponseModel("Field \"ibsn\" cannot be null", HttpStatus.BAD_REQUEST);
        }

        try {
            var unit = unitRepo.findByIbsn(bookUnitDTO.getIbsn());
            System.out.println(bookUnitDTO.getIbsn());
            if(unit.isEmpty()) {
                return new ResponseModel("Unit not found", HttpStatus.NOT_FOUND);
            }
            unitRepo.delete(unit.get());

            System.out.println("AQUI VEM O TITULO");
            System.out.println(bookUnitDTO.getTitle());

            var book = dbset.findByTitle(bookUnitDTO.getTitle());
            if(book.isEmpty()) {
                return new ResponseModel("Book not found", HttpStatus.NOT_FOUND);
            }
            book.get().setAvailableAmount(book.get().getAvailableAmount() - 1);
            dbset.save(book.get());

            return new ResponseModel("Unit deleted", HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseModel("ERROR: " + ex.getCause() + "\n" + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseModel getBookByTitle(String title) {
        Optional<Book> book = dbset.findByTitle(title);
        if(book.isEmpty() || book.get().getTitle().isEmpty()) {
            return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
        }

        JoinBookResponseModel data = new JoinBookResponseModel();

        data.setId(Optional.of(book.get().getId()));
        data.setTitle(book.get().getTitle());
        data.setAuthorName(book.get().getAuthor().getName());
        data.setPublisher( new PublisherResponse(
                book.get().getPublisher().getName(),
                book.get().getPublisher().getCnpj()
        ));
        data.setDescription(book.get().getDescription());
        data.setAvailableAmount(book.get().getAvailableAmount());
        data.setBookUnits(book.get().getUnits());

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel deleteBookById(UUID id) {
        var book = dbset.findById(id);
        if(book.isEmpty()) {
            return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
        }
        var units = unitRepo.findAllByBookId(book.get().getId());
        if ((long) units.size() > 0) {
            return new ResponseModel("This book has more than one unit registered, delete then first.", HttpStatus.FORBIDDEN);
        }
        try {
            dbset.delete(book.get());
            return new ResponseModel("Book has deleted.", HttpStatus.OK);

        }
        catch (Exception ex) {
            return new ResponseModel("ERROR: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseModel listAllIbsnsById(UUID id, Optional<Boolean> hideUnavailable) {
        var response = unitRepo.findAllByBookId(id);
        if( hideUnavailable.isPresent() && hideUnavailable.get().equals(true) ) {
            var data = this
                    .mappingUnits(response)
                    .stream()
                    .filter(x -> x.getBorrowing() == null)
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }
        var data = this.mappingUnits(response);
        return new ResponseModel(data, HttpStatus.OK);
    }

    ///WARNING: FOR DEBUG
    public ResponseModel listAllIBSNS(Optional<Boolean> hideUnavailable) {
        var response = unitRepo.findAll();
        if( hideUnavailable.isPresent() && hideUnavailable.get().equals(true) ) {
            var data = this
                    .mappingUnits(response)
                    .stream()
                    .filter(x -> x.getBorrowing() == null)
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.I_AM_A_TEAPOT);
        }
        var data = this.mappingUnits(response);
        return new ResponseModel(data, HttpStatus.I_AM_A_TEAPOT);
    }

    /**
     * @apiNote Mapeia uma coleção de BookUnit para uma coleção de UnitResponse
     **/
    private List<UnitResponse> mappingUnits(List<Unit> source) {
        List<UnitResponse> target = new ArrayList<>();
        for (Unit item : source) {
            var unit = new UnitResponse();
            BeanUtils.copyProperties(item, unit);
            target.add(unit);
        }
        return target;
    }
}
