package com.rs.elasticsearchservice.service;

import com.rs.elasticsearchservice.databaserepository.EmployeeRepository;
import com.rs.elasticsearchservice.databaserepository.LoginTokenRepository;
import com.rs.elasticsearchservice.exception.NotFoundException;
import com.rs.elasticsearchservice.model.LoginToken;
import com.rs.elasticsearchservice.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    private final LoginTokenRepository loginTokenRepository;

    private final EncryptionUtil encryptionUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException("Employee with given credentials does not exist."));
    }

    public String getRedirectUrl(String token) throws Exception {
        var fetchToken = UUID.randomUUID().toString();
        var newLoginToken = new LoginToken(encryptionUtil.encrypt(token), fetchToken);
        loginTokenRepository.save(newLoginToken);

        return "http://127.0.0.1:5175/login/" + fetchToken;
    }

    public String fetchToken(String fetchToken) throws Exception {
        var loginToken = loginTokenRepository.fetchToken(fetchToken).orElseThrow(() ->
                new NotFoundException("Invalid fetch token."));
//        loginTokenRepository.delete(loginToken);
        return encryptionUtil.decrypt(loginToken.getRealToken());
    }
}
