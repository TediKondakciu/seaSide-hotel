package com.springbootproject.seasidehotel.api;


import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequestMapping(value = "/roles")
public interface RoleApi {

    @Operation(summary = "Creates a Role", operationId = "createNewRole", description = "This operation creates a Role entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = String.class)))})
    @RequestMapping(value = "/create-new-role", method = RequestMethod.POST)
    ResponseEntity<String> createNewRole(@Parameter(description = "The Role to be created", required = true) @Valid @RequestBody Role role);

    @Operation(summary = "Deletes a Role", operationId = "deleteRole", description = "This operation deletes a Role entity.")
    @RequestMapping(value = "/delete/{roleId}", method = RequestMethod.DELETE)
    void deleteRole(@Parameter(description = "Identifier of the Role", required = true) @PathVariable("roleId") Long id);

    @Operation(summary = "Gets Role objects", operationId = "getAllRoles", description = "This operation gets Role entities.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Role.class))))})
    @RequestMapping(value = "/all-roles", method = RequestMethod.GET)
    ResponseEntity<List<Role>> getAllRoles();

    @Operation(summary= "Assigns Role object to User object", operationId = "assignRoleToUser", description = "This operation assigns a Role entity to a User entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = User.class)))})
    @RequestMapping(value = "/assign-role-to-user", method = RequestMethod.POST)
    User assignRoleToUser(@Parameter(description = "Identifier of the User") @Valid @RequestParam("userId") Long userId, @Parameter(description = "Identifier of the Role") @Valid @RequestParam("roleId") Long roleId);

    @Operation(summary = "Removes a User object from a Role object", operationId = "removeUserFromRole", description = "This operation removes a User entity from a Role entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = User.class)))})
    @RequestMapping(value = "/remove-user-from-role", method = RequestMethod.POST)
    User removeUserFromRole(@Parameter(description = "Identifier of the User") @Valid @RequestParam("userId") Long userId, @Parameter(description = "Identifier of the Role") @Valid @RequestParam("roleId") Long roleId);

    @Operation(summary = "Removes all User objects from a Role object", operationId = "removeAllUsersFromRole", description = "This operation removes all User entities from a Role entity.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Role.class)))})
    @RequestMapping(value = "/remove-all-users-from-role/{roleId}", method = RequestMethod.POST)
    Role removeAllUsersFromRole(@Parameter(description = "Identifier of the Role", required = true) @PathVariable("roleId") Long id);
}
