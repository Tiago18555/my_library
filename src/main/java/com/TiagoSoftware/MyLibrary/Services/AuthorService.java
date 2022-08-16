package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.PublisherUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.AuthorRepository;
import com.TiagoSoftware.MyLibrary.Repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    @Autowired
    private final AuthorRepository dbset;

    @Autowired
    private final BookRepository bookRepo;

    public AuthorService(AuthorRepository dbset, BookRepository bookRepo) {
        this.dbset = dbset;
        this.bookRepo = bookRepo;
    }

    @Transactional
    public ResponseModel registerNewAuthor(AuthorDTO authorDTO) {
        var author = new Author();
        BeanUtils.copyProperties(authorDTO, author);
        try{
            if ( dbset.findByName(author.getName()).isPresent() ) {
                return new ResponseModel("This author already exists", HttpStatus.FORBIDDEN);
            };
            return new ResponseModel(dbset.save(author), HttpStatus.CREATED);
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public ResponseModel listAuthors(){
        try{
            return new ResponseModel(dbset.findAll().stream().collect(Collectors.toList()), HttpStatus.OK);
        }
        catch(Exception ex){
            throw ex;
        }
    }

    @Transactional
    public ResponseModel deleteAuthorById(UUID id) {
        var author = dbset.findById(id);
        if(author.isEmpty()) {
            return new ResponseModel("Author not found.", HttpStatus.NOT_FOUND);
        }

        Boolean hasAnyBookAssociated = bookRepo
                .findAll()
                .stream()
                .anyMatch(x -> x.getAuthor().getId() == id);

        if(hasAnyBookAssociated) {
            return new ResponseModel("Constraint failed error: This author has books associated.", HttpStatus.FORBIDDEN);
        }

        dbset.delete(author.get());

        return new ResponseModel("Author has deleted.", HttpStatus.OK);
    }

    @Transactional
    public ResponseModel editAuthor(AuthorUpdateDTO authorUpdateDTO) {
        var author = new Author();

        var foundAuthor = dbset.findById(authorUpdateDTO.getId());

        if( foundAuthor.isPresent() ) {
            author.setId(foundAuthor.get().getId());

            author.setName(
                    authorUpdateDTO.getName().equals(foundAuthor.get().getName())
                            ? foundAuthor.get().getName()
                            : authorUpdateDTO.getName()
            );
        }

        try {
            var data = dbset.save(author);
            return new ResponseModel(data, HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
