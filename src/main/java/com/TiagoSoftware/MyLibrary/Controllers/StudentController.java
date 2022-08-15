package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.BookDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.BookUpdateDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ClientUpdateDTO;
import com.TiagoSoftware.MyLibrary.Services.BookService;
import com.TiagoSoftware.MyLibrary.Services.StudentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity listStudent() {
        var response = studentService.listStudents();

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PutMapping
    @ApiOperation(value="Altera um registro de aluno cadastrado")
    public ResponseEntity editStudent(@RequestBody @Valid ClientUpdateDTO clientUpdateDTO) {
        var response = studentService.editStudent(clientUpdateDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("/{title}")
    @ApiOperation(value="Exibe as informações detalhadas de um aluno")
    public ResponseEntity getStudentById(@PathVariable String name) {
        var response = studentService.getStudentByName(name);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Altera o aluno para inativo")
    public ResponseEntity deleteStudent(@PathVariable UUID id) {
        var response = studentService.deleteStudentById(id);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
