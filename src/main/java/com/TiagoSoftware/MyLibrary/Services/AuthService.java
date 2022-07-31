package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Configuration.SecurityConfig;
import com.TiagoSoftware.MyLibrary.Models.Entity.Auth;
import com.TiagoSoftware.MyLibrary.Models.DTO.AuthDTO;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.AuthRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final SecurityConfig bcrypt;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthService(AuthRepository dbset, JwtService jwtService, SecurityConfig bcrypt, AuthenticationManager authenticationManager) {
        this.dbset = dbset;
        this.jwtService = jwtService;
        this.bcrypt = bcrypt;
        this.authenticationManager = authenticationManager;
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

            String hash = bcrypt.passwordEncoder().encode(auth.getPassword());
            auth.setPassword( hash );

            return new ResponseModel(dbset.save(auth), HttpStatus.CREATED);
        }
        catch(Exception ex){
            //throw ex;
            return new ResponseModel("TRY CATCH REGISTER: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseModel Login(AuthDTO authDTO){
        var auth = new Auth();
        BeanUtils.copyProperties(authDTO, auth);

        try{
            if(auth.getUsername() == null || auth.getPassword() == null) {
                return new ResponseModel("Error: Username or password must not be empty!", HttpStatus.BAD_REQUEST);
            }
            Optional<Auth> res = Optional.of(dbset.findByUsername(auth.getUsername()).get());

            if(!bcrypt.passwordEncoder().matches(auth.getPassword(), res.get().getPassword() ) ) {
                return new ResponseModel("Error: Incorrect username or password!", HttpStatus.UNAUTHORIZED);
            }

            Optional<Auth> user = Optional.ofNullable(dbset
                    .findByUsername(auth.getUsername())
                    .orElseThrow(() -> new RuntimeException("NULL NESSE FIND")));

            String token = jwtService.generateToken( user.get().getUsername() );

            /*
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth, null)
            );
            */


            return new ResponseModel(
                    token,
                    HttpStatus.OK,
                    Optional.of(token)
            );
        }
        catch (BadCredentialsException ex) {
            //throw ex;
            return new ResponseModel("Unauthorized in TRYCATCH.: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            //throw ex;
            return new ResponseModel(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
