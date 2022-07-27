package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.AuthDTO;
import com.TiagoSoftware.MyLibrary.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity RegisterNewUser(@RequestBody @Valid AuthDTO authDTO){
        var response = authService.RegisterNewUser(authDTO);
        return ResponseEntity
                .status(response.getHttpstatus())
                .body(response.getData());
    }

    @PostMapping(path = "/login")
    public ResponseEntity Auth(@RequestBody @Valid AuthDTO authDTO){
        var response = authService.Login(authDTO);
        return ResponseEntity
                .status(response.getHttpstatus())
                .body(response.getData());
    }
}
