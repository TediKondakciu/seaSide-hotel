package com.springbootproject.seasidehotel.security.user;

import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Tedi Kondakçiu
 */

@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return HotelUserDetails.buildUserDetails(user);
    }
}
