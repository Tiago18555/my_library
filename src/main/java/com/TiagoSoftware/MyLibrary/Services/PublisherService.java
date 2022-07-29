package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthorDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.PublisherDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.AuthorRepository;
import com.TiagoSoftware.MyLibrary.Repositories.PublisherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    @Autowired
    PublisherRepository dbset;

    public PublisherService(PublisherRepository dbset) {
        this.dbset = dbset;
    }

    @Transactional
    public ResponseModel registerNewPublisher(PublisherDTO publisherDTO) {
        var publisher = new Publisher();
        BeanUtils.copyProperties(publisherDTO, publisher);
        try{
            if ( dbset.findByName(publisher.getName()) != null ) {
                return new ResponseModel("This publisher already exists", HttpStatus.FORBIDDEN);
            };
            String currentCNPJ = publisher.getCnpj();
            publisher.setCnpj(
                    currentCNPJ.trim()
                            .replace("/", "")
                            .replace("-", "")
                            .replace(".", "")
            );
            return new ResponseModel(dbset.save(publisher), HttpStatus.CREATED);
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public ResponseModel listPublishers(){
        try{
            return new ResponseModel(dbset.findAll().stream().collect(Collectors.toList()), HttpStatus.OK);
        }
        catch(Exception ex){
            throw ex;
        }
    }
}
