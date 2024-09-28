package com.springbootproject.seasidehotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

@Schema(description = "Role is an abstract entity that defines the level of access and permissions that all concrete users have.")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Name cannot be null!")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<>();

    public Role(String name){
        this.name = name;
    }

    @JsonIgnore
    public Collection<User> getUsers(){
        return users;
    }
    public void assignRoleToUser(User user){
        user.getRoles().add(this);
        this.getUsers().add(user);
    }

    public void removeUserFromRole(User user){
        user.getRoles().remove(this);
        this.getUsers().remove(user);
    }

    public void removeAllUsersFromRole(){
        if(this.getUsers() != null){
            List<User> roleUsers = this.getUsers().stream().toList();
            roleUsers.forEach(this :: removeUserFromRole);
        }
    }

    public String getName(){
        return name != null ? name : "";
    }
}
