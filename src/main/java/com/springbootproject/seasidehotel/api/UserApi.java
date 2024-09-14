package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Validated
@Api(value = "user")
@RequestMapping(value = "/users")
public interface UserApi {

    @ApiOperation(value = "Registers a User", nickname = "registerUser", notes = "This operation creates a User entity.", response = String.class)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    ResponseEntity<String> registerUser(@ApiParam(value = "The User to be registered", required = true) @Valid @RequestBody User user);

    @ApiOperation(value = "Gets User objects", nickname = "getUsers", notes = "This operation gets User entities.", response = User.class, responseContainer = "List")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    ResponseEntity<List<User>> getUsers();

    @ApiOperation(value = "Gets a User", nickname = "getUserByEmail", notes = "This operation gets a User entity by its email property.", response = User.class)
    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    ResponseEntity<?> getUserByEmail(@ApiParam(value = "Email of the User", required = true) @PathVariable("email") String email);

    @ApiOperation(value = "Deletes a User", nickname = "deleteUser", notes = "This operation deletes a User entity.", response = String.class)
    @RequestMapping(value = "/delete/{email}", method = RequestMethod.DELETE)
    ResponseEntity<String> deleteUser(@ApiParam(value = "Email of the User", required = true) @PathVariable String email);
}
