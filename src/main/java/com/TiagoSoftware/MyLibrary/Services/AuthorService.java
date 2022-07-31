package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.AuthorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    @Autowired
    private final AuthorRepository dbset;

    public AuthorService(AuthorRepository dbset) {
        this.dbset = dbset;
    }

    @Transactional
    public ResponseModel registerNewAuthor(AuthorDTO authorDTO) {
        var author = new Author();
        BeanUtils.copyProperties(authorDTO, author);
        try{
            if ( dbset.findByName(author.getName()) != null ) {
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
}
