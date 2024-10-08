package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.exception.RoleAlreadyExistsException;
import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.service.IRoleService;
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
@RequestMapping("/roles")
public class RoleController implements RoleApi{
    private final IRoleService roleService;

    @Autowired
    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public ResponseEntity<String> createNewRole(@Parameter(description = "The Role to be created", required = true) @Valid @RequestBody Role role){
        try{
            roleService.createRole(role);
            return new ResponseEntity<>("New role successfully created!", HttpStatus.OK);
        }catch(RoleAlreadyExistsException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    public void deleteRole(@Parameter(description = "Identifier of the Role", required = true) @PathVariable("roleId") Long id){
        roleService.deleteRole(id);
    }

    @Override
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @Override
    public User assignRoleToUser(@Parameter(description = "Identifier of the User") @Valid @RequestParam("userId") Long userId, @Parameter(description = "Identifier of the Role") @Valid @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }

    @Override
    public User removeUserFromRole(@Parameter(description = "Identifier of the User") @Valid @RequestParam("userId") Long userId, @Parameter(description = "Identifier of the Role") @Valid @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @Override
    public Role removeAllUsersFromRole(@Parameter(description = "Identifier of the Role", required = true) @PathVariable("roleId") Long id){
        return roleService.removeAllUsersFromRole(id);
    }
}
