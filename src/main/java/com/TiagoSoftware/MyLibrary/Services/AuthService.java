package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Configuration.BcryptConfiguration;
import com.TiagoSoftware.MyLibrary.Models.Entity.Auth;
import com.TiagoSoftware.MyLibrary.Models.DTO.AuthDTO;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.AuthRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private final AuthRepository dbset;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final BcryptConfiguration bcrypt;

    public AuthService(AuthRepository dbset, JwtService jwtService, BcryptConfiguration bcrypt) {
        this.dbset = dbset;
        this.jwtService = jwtService;
        this.bcrypt = bcrypt;
    }

    @Transactional
    public ResponseModel RegisterNewUser(AuthDTO authDTO) {
        var auth = new Auth();
        BeanUtils.copyProperties(authDTO, auth);
        try{
            if(auth.getUsername() == null || auth.getPassword() == null) {
                return new ResponseModel("Error: Username or password must not be empty!", HttpStatus.BAD_REQUEST);
            }
            if(dbset.existsByUsername(auth.getUsername())){
                return new ResponseModel("Error: User is already in use!", HttpStatus.UNAUTHORIZED);
            }

            String hash = bcrypt.bcryptEncoder().encode(auth.getPassword());
            auth.setPassword( hash );

            System.out.println("HASHSIZE: " + hash.length() + "\nHASH: " + hash);


            return new ResponseModel(dbset.save(auth), HttpStatus.CREATED);
        }
        catch(Exception ex){
            throw ex;
            //return new ResponseModel(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseModel Login(AuthDTO authDTO){
        var auth = new Auth();
        BeanUtils.copyProperties(authDTO, auth);

        try{
            if(auth.getUsername() == null || auth.getPassword() == null) {
                return new ResponseModel("Error: Username or password must not be empty!", HttpStatus.BAD_REQUEST);
            }
            if(!dbset.existsByUsernameAndPassword(auth.getUsername(), auth.getPassword())){
                return new ResponseModel("Error: Incorrect username or password!", HttpStatus.UNAUTHORIZED);
            }

            Auth user = dbset
                    .findByUsername(auth.getUsername());
            String token = jwtService.generateToken( user.getUserId().toString() );
            return new ResponseModel(token, HttpStatus.OK);
        }
        catch(Exception ex){
            throw ex;
            //return new ResponseModel(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
