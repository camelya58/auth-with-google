package com.github.camelya58.auth_with_google.service;

import com.github.camelya58.auth_with_google.model.User;
import com.github.camelya58.auth_with_google.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Class UserService allows to find user by username, name, google name and google username.
 *
 * @author Kamila Meshcheryakova
 * created 17.07.2020
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFindByUsername = userRepository.findByUsername(username);
        User userFindByName = userRepository.findByName(username);
        User userFindByGoogleName = userRepository.findByGoogleName(username);
        User userFindByGoogleUsername = userRepository.findByGoogleUsername(username);

        if (userFindByUsername != null) {
            return userFindByUsername;
        }

        if (userFindByName != null) {
            return userFindByName;
        }

        if (userFindByGoogleUsername != null) {
            return userFindByGoogleUsername;
        }

        if (userFindByGoogleName != null) {
            return userFindByGoogleName;
        }

        return null;
    }
}
