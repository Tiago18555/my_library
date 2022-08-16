package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.PublisherDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.PublisherUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.BookRepository;
import com.TiagoSoftware.MyLibrary.Repositories.PublisherRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    @Autowired
    private final PublisherRepository dbset;

    @Autowired
    private final BookRepository bookRepo;

    public PublisherService(PublisherRepository dbset, BookRepository bookRepo) {
        this.dbset = dbset;
        this.bookRepo = bookRepo;
    }

    @Transactional
    public ResponseModel registerNewPublisher(PublisherDTO publisherDTO) {
        var publisher = new Publisher();
        BeanUtils.copyProperties(publisherDTO, publisher);
        try{
            if ( dbset.findByName(publisher.getName()).isPresent() ) {
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

    @Transactional
    public ResponseModel deletePublisherById(UUID id) {
        var publisher = dbset.findById(id);
        if(publisher.isEmpty()) {
            return new ResponseModel("Publisher not found.", HttpStatus.NOT_FOUND);
        }

        Boolean hasAnyBookAssociated = bookRepo
                .findAll()
                .stream()
                .anyMatch(x -> x.getPublisher().getId() == id);

        if(hasAnyBookAssociated) {
            return new ResponseModel("Constraint failed error: This publisher has books associated.", HttpStatus.FORBIDDEN);
        }

        dbset.delete(publisher.get());

        return new ResponseModel("Publisher has deleted.", HttpStatus.OK);
    }

    @Transactional
    public ResponseModel editPublisher(@NotNull PublisherUpdateDTO publisherUpdateDTO) {
        var publisher = new Publisher();

        var foundPublisher = dbset
                .findByCnpj(publisherUpdateDTO
                        .getCnpj()
                        .trim()
                        .replace("-", "")
                        .replace("/", "")
                        .replace(".", "")
                );

        if( foundPublisher.isEmpty() ) {
            return new ResponseModel("Publisher not found", HttpStatus.NOT_FOUND);
        }
        publisher.setId(foundPublisher.get().getId());

        publisher.setName(
                publisherUpdateDTO.getName() == foundPublisher.get().getName()
                        ? foundPublisher.get().getName()
                        : publisherUpdateDTO.getName()
        );

        publisher.setCnpj(
                publisherUpdateDTO.getCnpj() == foundPublisher.get().getCnpj()
                        ? foundPublisher.get().getCnpj()
                        : publisherUpdateDTO.getCnpj()
        );
        System.out.println("CNPJ: " + publisher.getCnpj());

        try {
            var data = dbset.save(publisher);
            return new ResponseModel(data, HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
