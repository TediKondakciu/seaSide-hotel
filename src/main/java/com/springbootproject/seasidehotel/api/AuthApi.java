package com.springbootproject.seasidehotel.api;


import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Validated
@RequestMapping(value = "/auth")
public interface AuthApi {

    @Operation(summary = "Registers a User", operationId = "register-user", description = "This operation creates a User entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
    @RequestMapping(value = "/register-user", method = RequestMethod.POST)
    ResponseEntity<?> registerUser(@Parameter(description = "The User to be registered", required = true) @Valid @RequestBody User user);

    @Operation(summary = "Login a User", operationId = "login", description = "This operation logs in a User entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    ResponseEntity<?> authenticateUser(@Parameter(description = "The User data to authenticate", required = true) @Valid @RequestBody LoginRequest request);
}
