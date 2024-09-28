package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Validated
@RequestMapping(value = "/users")
public interface UserApi {

    @Operation(summary = "Gets User objects", operationId = "getUsers", description = "This operation gets User entities.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))))})
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<List<User>> getUsers();

    @Operation(summary = "Gets a User", operationId = "getUserByEmail", description = "This operation gets a User entity by its email property.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = User.class)))})
    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> getUserByEmail(@Parameter(description = "Email of the User", required = true) @PathVariable("email") String email);

    @Operation(summary = "Deletes a User", operationId = "deleteUser", description = "This operation deletes a User entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = String.class)))})
    @RequestMapping(value = "/delete/{email}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email == principal.username)")
    ResponseEntity<String> deleteUser(@Parameter(description = "Email of the User", required = true) @PathVariable String email);
}
