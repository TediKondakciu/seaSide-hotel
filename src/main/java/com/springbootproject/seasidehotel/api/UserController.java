package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.exception.UserAlreadyExistsException;
import com.springbootproject.seasidehotel.exception.UsernameNotFoundException;
import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.service.IUserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

@RestController
public class UserController implements UserApi{
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.FOUND);
    }


    @Override
    public ResponseEntity<?> getUserByEmail(@Parameter(description = "Email of the User", required = true) @PathVariable("email") String email){
        try{
            return new ResponseEntity<>(userService.getUser(email), HttpStatus.OK);
        }catch(UsernameNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }catch(Exception ex){
            return new ResponseEntity<>("Error fetching user!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(@Parameter(description = "Email of the User", required = true) @PathVariable String email){
        try{
            userService.deleteUser(email);
            return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
        }catch (UsernameNotFoundException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }catch(Exception ex){
            return new ResponseEntity<>("Error deleting user!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
