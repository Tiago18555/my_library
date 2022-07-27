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

@Service
public class AuthorService {
    @Autowired
    AuthorRepository dbset;

    public AuthorService(AuthorRepository dbset) {
        this.dbset = dbset;
    }

    @Transactional
    public ResponseModel RegisterNewAuthor(AuthorDTO authorDTO) {
        var author = new Author();
        BeanUtils.copyProperties(authorDTO, author);
        try{
            return new ResponseModel(dbset.save(author), HttpStatus.CREATED);
        }
        catch(Exception ex){
            throw ex;
            //return new ResponseModel(null, HttpStatus.BAD_REQUEST);
        }
    }
}
