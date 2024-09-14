package com.springbootproject.seasidehotel.response;

import lombok.Data;

import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

@Data
public class JwtResponse {
    private Long id;
    private String email;
    private String token;
    private List<String> roles;

    public JwtResponse(Long id, String email, String token, List<String> roles) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.roles = roles;
    }
}
