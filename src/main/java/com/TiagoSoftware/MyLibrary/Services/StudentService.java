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
import java.util.List;
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
            client.setIsProfessor(false);
            client.setIsInactive(false);
            var data = dbset.save(client);
            return new ResponseModel(data, HttpStatus.CREATED);
        }
        catch (Exception ex) {
            //return new ResponseModel(null, HttpStatus.BAD_REQUEST);
            throw ex;
        }
    }

    public ResponseModel listStudents(Optional<Boolean> showInactive) {
        List<Client> data;

        if(showInactive.isPresent() && showInactive.get()) {
            data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> !x.isProfessor)
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }

        data = dbset
                .findAll()
                .stream()
                .filter(x -> !x.isInactive)
                .filter(x -> !x.isProfessor)
                .collect(Collectors.toList());

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
            student.setIsInactive(false);
            student.setIsProfessor(false);
            student.setId(foundStudent.getId());

            return new ResponseModel(dbset.save(student), HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseModel getStudentByCpf(String cpf){
        Client data = dbset.findByCpf(cpf);
        if(data == null || data.getCpf().isEmpty()) {
            return new ResponseModel("Student not found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel deleteStudentById(UUID id, Optional<Boolean> restore) {
        var student = dbset.findById(id);
        if(student.isEmpty()) {
            return new ResponseModel("Student not found.", HttpStatus.NOT_FOUND);
        }

        //Student.isInactive == false  if there's a parameter of restore = true
        student.get().setIsInactive(restore.isPresent() && restore.get().equals(true) ? false : true);

        dbset.save(student.get());
        return new ResponseModel(student.get().getIsInactive() == true ? "Student has deleted." : "Student has restored", HttpStatus.OK);
    }
}
