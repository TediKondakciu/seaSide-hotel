package com.springbootproject.seasidehotel.api;

import com.springbootproject.seasidehotel.exception.UserAlreadyExistsException;
import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.request.LoginRequest;
import com.springbootproject.seasidehotel.response.JwtResponse;
import com.springbootproject.seasidehotel.security.user.HotelUserDetails;
import com.springbootproject.seasidehotel.security.jwt.JwtUtils;
import com.springbootproject.seasidehotel.service.IUserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

@RestController
public class AuthController implements AuthApi{

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(IUserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<?> registerUser(@Parameter(description = "The User to be registered", required = true) @RequestBody User user){
        try{
            userService.registerUser(user);
            return new ResponseEntity<>("New user successfully registered!", HttpStatus.OK);
        }catch (UserAlreadyExistsException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> authenticateUser(@Parameter(description = "The User data to authenticate", required = true) @Valid @RequestBody LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(userDetails.getId(), userDetails.getEmail(), jwt, roles));
    }
}
