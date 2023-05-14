package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.ClientDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
import com.TiagoSoftware.MyLibrary.Models.Responses.ListClients.ListClientsResponseModel;
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
        List<ListClientsResponseModel> data;

        if(showInactive.isPresent() && showInactive.get()) {
            data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> !x.isProfessor)
                    .map(x -> {
                        var target = new ListClientsResponseModel();
                        BeanUtils.copyProperties(x, target);
                        return target;
                    })
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }

        data = dbset
                .findAll()
                .stream()
                .filter(x -> !x.isInactive)
                .filter(x -> !x.isProfessor)
                .map(x -> {
                    var target = new ListClientsResponseModel();
                    BeanUtils.copyProperties(x, target);
                    return target;
                })
                .collect(Collectors.toList());

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel editStudent(ClientUpdateDTO clientUpdateDTO) {
        var student = new Client();

        try {
            var foundStudent = dbset.findByCpf(clientUpdateDTO.getCpf());
            if(foundStudent.isEmpty()) {
                return new ResponseModel("Student not found.", HttpStatus.NOT_FOUND);
            }

            if(foundStudent.get().getIsProfessor().equals(true)){
                return new ResponseModel("This cpf belongs to a professor.", HttpStatus.BAD_REQUEST);
            }

            student.setCpf(foundStudent.get().getCpf());
            student.setName(clientUpdateDTO.getName() != null ? clientUpdateDTO.getName() : foundStudent.get().getName());
            student.setLoan(foundStudent.get().getLoan());
            student.setBorrowings(foundStudent.get().getBorrowings());
            student.setIsInactive(false);
            student.setIsProfessor(false);
            student.setId(foundStudent.get().getId());

            return new ResponseModel(dbset.save(student), HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseModel getStudentByCpf(String cpf){
        var data = dbset.findByCpf(cpf);
        if(data == null || data.get().getCpf().isEmpty()) {
            return new ResponseModel("Student not found.", HttpStatus.NOT_FOUND);
        }

        if(data.get().isProfessor.equals(true)) {
            return new ResponseModel("This cpf belongs to a professor.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel deleteStudentById(UUID id, Optional<Boolean> restore) {
        var student = dbset.findById(id);
        if(student.isEmpty()) {
            return new ResponseModel("Student not found.", HttpStatus.NOT_FOUND);
        }

        if(student.get().isProfessor.equals(true)) {
            return new ResponseModel("This id belongs to a professor.", HttpStatus.BAD_REQUEST);
        }

        student.get().setIsInactive(restore.isPresent() && restore.get().equals(true) ? false : true);

        dbset.save(student.get());
        return new ResponseModel(student.get().getIsInactive() == true ? "Student has deleted." : "Student has restored", HttpStatus.OK);
    }
}
