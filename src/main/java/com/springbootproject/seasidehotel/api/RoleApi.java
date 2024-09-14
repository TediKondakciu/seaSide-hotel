package com.springbootproject.seasidehotel.api;


import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Api(value = "role")
@RequestMapping(value = "/roles")
public interface RoleApi {

    @ApiOperation(value = "Creates a Role", nickname = "createNewRole", notes = "This operation creates a Role entity.", response = String.class)
    @RequestMapping(value = "/create-new-role", method = RequestMethod.POST)
    ResponseEntity<String> createNewRole(@ApiParam(value = "The Role to be created", required = true) @Valid @RequestBody Role role);

    @ApiOperation(value = "Deletes a Role", nickname = "deleteRole", notes = "This operation deletes a Role entity.")
    @RequestMapping(value = "/delete/{roleId}", method = RequestMethod.DELETE)
    void deleteRole(@ApiParam(value = "Identifier of the Role", required = true) @PathVariable("roleId") Long id);

    @ApiOperation(value = "Gets Role objects", nickname = "getAllRoles", notes = "This operation gets Role entities.", response = Role.class, responseContainer = "List")
    @RequestMapping(value = "/all-roles", method = RequestMethod.GET)
    ResponseEntity<List<Role>> getAllRoles();

    @ApiOperation(value = "Assigns Role object to User object", nickname = "assignRoleToUser", notes = "This operation assigns a Role entity to a User entity.", response = User.class)
    @RequestMapping(value = "/assign-role-to-user", method = RequestMethod.POST)
    User assignRoleToUser(@ApiParam(value = "Identifier of the User") @Valid @RequestParam("userId") Long userId, @ApiParam(value = "Identifier of the Role") @Valid @RequestParam("roleId") Long roleId);

    @ApiOperation(value = "Removes a User object from a Role object", nickname = "removeUserFromRole", notes = "This operation removes a User entity from a Role entity.", response = User.class)
    @RequestMapping(value = "/remove-user-from-role", method = RequestMethod.POST)
    User removeUserFromRole(@ApiParam(value = "Identifier of the User") @Valid @RequestParam("userId") Long userId, @ApiParam(value = "Identifier of the Role") @Valid @RequestParam("roleId") Long roleId);

    @ApiOperation(value = "Removes all User objects from a Role object", nickname = "removeAllUsersFromRole", notes = "This operation removes all User entities from a Role entity.", response = Role.class)
    @RequestMapping(value = "/remove-all-users-from-role/{roleId}", method = RequestMethod.POST)
    Role removeAllUsersFromRole(@ApiParam(value = "Identifier of the Role", required = true) @PathVariable("roleId") Long id);
}
