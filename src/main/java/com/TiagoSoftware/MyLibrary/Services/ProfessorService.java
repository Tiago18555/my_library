package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.ClientDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Client;
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
public class ProfessorService {

    @Autowired
    private final ClientRepository dbset;

    public ProfessorService(ClientRepository dbset) {
        this.dbset = dbset;
    }

    @Transactional
    public ResponseModel registerNewProfessor(ClientDTO clientDTO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientDTO, client);

        try {
            if (dbset.findByCpf(client.getCpf()) != null) {
                return new ResponseModel("This cpf has already registered", HttpStatus.FORBIDDEN);
            }
            client.setIsProfessor(true);
            client.setIsInactive(false);
            var data = dbset.save(client);
            return new ResponseModel(data, HttpStatus.CREATED);
        }
        catch (Exception ex) {
            //return new ResponseModel(null, HttpStatus.BAD_REQUEST);
            throw ex;
        }
    }

    public ResponseModel listProfessors(Optional<Boolean> showInactive) {
        List<Client> data;

        if(showInactive.isPresent() && showInactive.get()) {
            data = dbset
                    .findAll()
                    .stream()
                    .filter(x -> x.isProfessor)
                    .collect(Collectors.toList());
            return new ResponseModel(data, HttpStatus.OK);
        }

        data = dbset
                .findAll()
                .stream()
                .filter(x -> !x.isInactive)
                .filter(x -> x.isProfessor)
                .collect(Collectors.toList());

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel editProfessor(ClientUpdateDTO clientUpdateDTO) {
        var professor = new Client();

        try {
            Client foundProfessor = dbset.findByCpf(clientUpdateDTO.getCpf());
            if(foundProfessor == null) {
                return new ResponseModel("Professor not found.", HttpStatus.NOT_FOUND);
            }

            if(foundProfessor.getIsProfessor().equals(false)){
                return new ResponseModel("This cpf belongs to a student.", HttpStatus.BAD_REQUEST);
            }

            professor.setCpf(foundProfessor.getCpf());
            professor.setName(clientUpdateDTO.getName() != null ? clientUpdateDTO.getName() : foundProfessor.getName());
            professor.setLoan(foundProfessor.getLoan());
            professor.setBorrowings(foundProfessor.getBorrowings());
            professor.setIsInactive(false);
            professor.setIsProfessor(true);
            professor.setId(foundProfessor.getId());

            return new ResponseModel(dbset.save(professor), HttpStatus.OK);
        }
        catch (Exception ex) {
            //throw ex;
            return new ResponseModel("An error occurs: " + ex.getMessage() + "\n" + ex.getCause(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseModel getProfessorByCpf(String cpf){
        Client data = dbset.findByCpf(cpf);
        if(data == null || data.getCpf().isEmpty()) {
            return new ResponseModel("Professor not found.", HttpStatus.NOT_FOUND);
        }

        if(data.isProfessor.equals(false)) {
            return new ResponseModel("This cpf belongs to a student.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseModel(data, HttpStatus.OK);
    }

    @Transactional
    public ResponseModel deleteProfessorById(UUID id, Optional<Boolean> restore) {
        var professor = dbset.findById(id);
        if(professor.isEmpty()) {
            return new ResponseModel("Professor not found.", HttpStatus.NOT_FOUND);
        }

        if(professor.get().isProfessor.equals(false)) {
            return new ResponseModel("This id belongs to a student.", HttpStatus.BAD_REQUEST);
        }

        //Student.isInactive == false  if there's a parameter of restore = true
        professor.get().setIsInactive(restore.isPresent() && restore.get().equals(true) ? false : true);

        dbset.save(professor.get());
        return new ResponseModel(professor.get().getIsInactive() == true ? "Professor has deleted." : "Professor has restored", HttpStatus.OK);
    }
}
