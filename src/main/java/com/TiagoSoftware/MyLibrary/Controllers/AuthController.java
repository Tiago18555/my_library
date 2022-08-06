package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthDTO;
import com.TiagoSoftware.MyLibrary.Models.DTO.ValidateDTO;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value="API rest para bibliotecas")
public class AuthController {

    @Autowired
    private AuthService authService;

    //@PreAuthorize()
    @PostMapping(path = "/register")
    @ApiOperation(value="Registra um novo utilizador do sistema")
    public ResponseEntity<ResponseModel> registerNewUser(@RequestBody @Valid AuthDTO authDTO){
        var response = authService.registerNewUser(authDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PostMapping(path = "/login")
    @ApiOperation(value="Autentica um novo utilizador do sistema")
    public ResponseEntity<ResponseModel> auth(@RequestBody @Valid AuthDTO authDTO) throws Exception {
        var res = authService.login(authDTO);

        return new ResponseEntity<>(res, res.getHttpstatus());
    }

    @PostMapping(path = "/validate")
    @ApiOperation(value="Verifica se o usuário é ou permanece válido")
    public ResponseEntity<ResponseModel> validate(@RequestBody @Valid ValidateDTO validateDTO) throws Exception {
        var res = authService.validateJwt(validateDTO);

        return new ResponseEntity<>(res, res.getHttpstatus());
    }
}
