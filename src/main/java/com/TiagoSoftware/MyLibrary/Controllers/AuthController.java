package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthDTO;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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
        var response = authService.RegisterNewUser(authDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @PostMapping(path = "/login")
    @ApiOperation(value="Autentica um novo utilizador do sistema")
    public ResponseEntity<ResponseModel> auth(@RequestBody @Valid AuthDTO authDTO) throws Exception {
        var res = authService.Login(authDTO);

        //var response = new ResponseEntity<>(res.getData(), res.getHttpstatus());

        return new ResponseEntity<>(res, res.getHttpstatus());

                //.header( HttpHeaders.AUTHORIZATION, res.getToken().get() )
                //.body(response);

        //return new ResponseEntity<>(response, response.getHttpstatus() );
    }
}
