package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.ClientDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Author;
import com.TiagoSoftware.MyLibrary.Models.Entity.Book;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
import com.TiagoSoftware.MyLibrary.Models.Entity.Publisher;
import com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook.JoinBookResponseModel;
import com.TiagoSoftware.MyLibrary.Models.Responses.JoinBook.PublisherResponse;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private final ClientRepository dbset;

    public StudentService(ClientRepository dbset) {
        this.dbset = dbset;
    }

    @Transactional
    public ResponseModel registerNewStudent(ClientDTO clientDTO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);

        try {
            if (dbset.findByCpf(client.getCpf()) != null) {
                return new ResponseModel("This cpf has already registered", HttpStatus.FORBIDDEN);
            }
            var data = dbset.save(client);
            return new ResponseModel(data, HttpStatus.CREATED);
        }
        catch (Exception ex) {
            //return new ResponseModel(null, HttpStatus.BAD_REQUEST);
            throw ex;
        }
    }

    public ResponseModel listStudents() {
        var data = dbset.findAll().stream().collect(Collectors.toList());
        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel editStudent(ClientUpdateDTO clientUpdateDTO) {
        var student = new Client();

        try {
            Client foundStudent = dbset.findByCpf(clientUpdateDTO.getCpf());
            if(foundStudent == null) {
                return new ResponseModel("Student not found.", HttpStatus.NOT_FOUND);
            }

            student.setCpf(clientUpdateDTO.getCpf());
            student.setName(clientUpdateDTO.getName() != null ? clientUpdateDTO.getName() : foundStudent.getName());
            student.setLoan(clientUpdateDTO.getLoan() != 0 ? clientUpdateDTO.getLoan() : foundStudent.getLoan());
            student.setBooks(clientUpdateDTO.getBooks() != null ? clientUpdateDTO.getBooks() : foundStudent.getBooks());

            return new ResponseModel(dbset.save(student), HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseModel getStudentByName(String cpf){
        Client data = dbset.findByCpf(cpf);
        if(data == null || data.getCpf().isEmpty()) {
            return new ResponseModel("Book not found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel deleteStudentById(UUID id) {
        var student = dbset.findById(id);
        if(student == null) {
            return new ResponseModel("Student not found.", HttpStatus.NOT_FOUND);
        }

        student.get().setIsInactive(true);
        dbset.save(student.get());
        return new ResponseModel("Student has deleted.", HttpStatus.OK);
    }
}
