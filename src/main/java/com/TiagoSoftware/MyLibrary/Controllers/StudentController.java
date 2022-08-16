package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientUpdateDTO;
import com.TiagoSoftware.MyLibrary.Services.BookService;
import com.TiagoSoftware.MyLibrary.Services.StudentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    @ApiOperation(value="Registra um novo aluno")
    public ResponseEntity registerStudent(@RequestBody @Valid ClientDTO clientDTO) {
        var response = studentService.registerNewStudent(clientDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping
    @ApiOperation(value="Lista todos os alunos cadastrados")
    @ApiParam(value="Lista todos os alunos incluindo os \"inativos\" na base de dados")
    public ResponseEntity listStudent(@RequestParam Optional<Boolean> showInactive) {
        var response = studentService.listStudents(showInactive);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PutMapping
    @ApiOperation(value="Altera um registro de aluno cadastrado")
    public ResponseEntity editStudent(@RequestBody @Valid ClientUpdateDTO clientUpdateDTO) {
        var response = studentService.editStudent(clientUpdateDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("/{cpf}")
    @ApiOperation(value="Exibe as informações detalhadas de um aluno")
    public ResponseEntity getStudentById(@PathVariable String cpf) {
        var response = studentService.getStudentByCpf(cpf);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Altera o aluno para inativo")
    public ResponseEntity deleteStudent(@PathVariable UUID id, @RequestParam Optional<Boolean> restore) {
        var response = studentService.deleteStudentById(id, restore);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
