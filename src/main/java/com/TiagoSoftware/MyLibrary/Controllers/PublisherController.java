package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.PublisherDTO;
import com.TiagoSoftware.MyLibrary.Services.PublisherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/register")
    @ApiOperation(value="Registra uma nova editora")
    public ResponseEntity registerPublisher(@RequestBody @Valid PublisherDTO publisherDTO){
        var response = publisherService.registerNewPublisher(publisherDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value="Lista todos os editoras cadastradas")
    public ResponseEntity listPublishers(){
        var response = publisherService.listPublishers();

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
