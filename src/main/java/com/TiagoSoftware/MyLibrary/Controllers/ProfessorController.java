package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.ClientDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientUpdateDTO;
import com.TiagoSoftware.MyLibrary.Services.ConfigurationService;
import com.TiagoSoftware.MyLibrary.Services.ProfessorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/professor")
public class ProfessorController {
    @Autowired
    private final ProfessorService professorService;
    @Autowired
    private final ConfigurationService configurationService;

    public ProfessorController(ProfessorService professorService, ConfigurationService configurationService) {
        this.professorService = professorService;
        this.configurationService = configurationService;
    }


    @PostMapping("/register")
    @ApiOperation(value="Registra um novo professor")
    public ResponseEntity registerProfessor(@RequestBody @Valid ClientDTO clientDTO) {
        var response = professorService.registerNewProfessor(clientDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value="Lista todos os professores cadastrados")
    @ApiParam(value="Lista todos os professores incluindo os \"inativos\" na base de dados")
    public ResponseEntity listProfessor(@RequestParam Optional<Boolean> showInactive) {
        var response = professorService.listProfessors(showInactive);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PutMapping
    @ApiOperation(value="Altera um registro de professor cadastrado")
    public ResponseEntity editProfessor(@RequestBody @Valid ClientUpdateDTO clientUpdateDTO) {
        var response = professorService.editProfessor(clientUpdateDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("/{cpf}")
    @ApiOperation(value="Exibe as informações detalhadas de um professor")
    public ResponseEntity getProfessorById(@PathVariable String cpf) {
        var response = professorService.getProfessorByCpf(cpf);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Altera o professor para inativo")
    public ResponseEntity deleteProfessor(@PathVariable UUID id, @RequestParam Optional<Boolean> restore) {
        var response = professorService.deleteProfessorById(id, restore);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
