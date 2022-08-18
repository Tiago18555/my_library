package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook.JoinBookResponseModel;
import com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook.PublisherResponse;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.BookRepository;
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

    public BookService(BookRepository dbset) {
        this.dbset = dbset;
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

    public ResponseModel listBooks(Optional<String> author, Optional<String> publisher, Optional<Boolean> onlyAvailable){
        try{
            if(author.isPresent() && publisher.isPresent()) {
                return new ResponseModel("Use just one search filter per request", HttpStatus.BAD_REQUEST);
            }

            List<JoinBookResponseModel> data = new ArrayList<>();
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

            for(Book book : books){
                JoinBookResponseModel target = new JoinBookResponseModel();

                target.setId(Optional.of(book.getId()));
                target.setTitle(book.getTitle());
                target.setAuthorName(book.getAuthor().getName());
                target.setPublisher( new PublisherResponse(
                        book.getPublisher().getName(),
                        book.getPublisher().getCnpj()
                ));
                target.setDescription(book.getDescription());
                target.setAvailableAmount(book.getAvailableAmount());

                data.add(target);
            }

            return new ResponseModel(data, HttpStatus.OK);
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
        Book book = dbset.findByTitle(title);
        if(book == null || book.getTitle().isEmpty()) {
            return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
        }

        JoinBookResponseModel data = new JoinBookResponseModel();

        data.setId(Optional.of(book.getId()));
        data.setTitle(book.getTitle());
        data.setAuthorName(book.getAuthor().getName());
        data.setPublisher( new PublisherResponse(
                book.getPublisher().getName(),
                book.getPublisher().getCnpj()
        ));
        data.setDescription(book.getDescription());
        data.setAvailableAmount(book.getAvailableAmount());

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel deleteBookById(UUID id) {
        var book = dbset.findById(id);
        if(book.isEmpty()) {
            return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
        }
        dbset.delete(book.get());
        return new ResponseModel("Book has deleted.", HttpStatus.OK);
    }
}
