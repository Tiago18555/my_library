package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import com.TiagoSoftware.MyLibrary.Models.Responses.JoinBookResponseModel;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private final BookRepository dbset;

    public BookService(BookRepository dbset) {
        this.dbset = dbset;
    }
/*
    private Book BookSerializer(Class<? extends BaseBookDTO> bookDTO) {

        var book = new Book();

        Author author = new Author();
        author.setId(bookDTO.getAuthor());
        Publisher publisher = new Publisher();
        publisher.setId(bookDTO.getPublisher());

        book.setTitle(bookDTO.getTitle() != null ? bookDTO.getTitle() : null);

        book.setAuthor(author.getId() != null ? author : null);
        book.setPublisher(publisher.getId() != null ? publisher : null);

        book.setDescription(bookDTO.getDescription() != null ? bookDTO.getDescription() : null);
        book.setAvailableAmount(bookDTO.getAvailableAmount() == 0 ? bookDTO.getAvailableAmount() : 1);

        return book;
    }
*/


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
        book.setAvailableAmount(bookDTO.getAvailableAmount() == 0 ? bookDTO.getAvailableAmount() : 1);



        try {
            if (dbset.findByTitle(book.getTitle()) != null) {
                return new ResponseModel("This books is already exists. Did you meant to change his quantity?", HttpStatus.FORBIDDEN);
            }
            return new ResponseModel(dbset.save(book), HttpStatus.CREATED);
        }
        catch(Exception ex){
            // throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseModel listBooks(Optional<String> author, Optional<String> publisher){
        System.out.println(author.orElse(null) + "\n" + publisher.orElse(null));
        try{
            //JoinBookResponseModel joinBookResponseModel = new JoinBookResponseModel();
            if(author.isPresent() && publisher.isPresent()) {
                return new ResponseModel("Use just one search filter per request", HttpStatus.BAD_REQUEST);
            }
            if(author.isPresent()) {
                return new ResponseModel(dbset
                        .findAllCompletly()
                        .stream()
                        .filter( x -> !x.getAuthorName().equals( author.get() ) )
                    , HttpStatus.OK);
            }
            if(publisher.isPresent()) {
                return new ResponseModel(dbset
                        .findAllCompletly()
                        .stream()
                        .filter( x -> !x.getPublisherName().equals( publisher.get() ) )
                    , HttpStatus.OK
                );
            }
        //if it doesn't have any Query Parameter
            return new ResponseModel(dbset.findAll().stream().collect(Collectors.toList()), HttpStatus.OK);
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

        book.setTitle(bookDTO.getTitle() != null ? bookDTO.getTitle() : null);

        book.setAuthor(author.getId() != null ? author : null);
        book.setPublisher(publisher.getId() != null ? publisher : null);

        book.setDescription(bookDTO.getDescription() != null ? bookDTO.getDescription() : null);
        book.setAvailableAmount(bookDTO.getAvailableAmount() == 0 ? bookDTO.getAvailableAmount() : 1);

        try {
            Book foundBook = dbset.findByTitle(book.getTitle());
            if(foundBook == null) {
                return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
            }
            book.setTitle(book.getTitle() != null ? book.getTitle() : foundBook.getTitle());
            book.setAuthor(book.getAuthor() != null ? book.getAuthor() : foundBook.getAuthor());
            book.setPublisher(book.getPublisher() != null ? book.getPublisher() : foundBook.getPublisher());
            book.setAvailableAmount(book.getAvailableAmount() == 0 ? book.getAvailableAmount() : foundBook.getAvailableAmount());
            book.setDescription(book.getDescription() != null ? book.getDescription() : foundBook.getDescription());

            return new ResponseModel(dbset.save(book), HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseModel getBookByTitle(String title) {
        Book foundBook = dbset.findByTitle(title);
        if(foundBook == null || foundBook.getTitle().isEmpty()) {
            return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseModel(foundBook, HttpStatus.OK);
    }
}
