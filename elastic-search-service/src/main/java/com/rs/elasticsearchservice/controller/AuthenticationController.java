package com.rs.elasticsearchservice.controller;

import com.rs.elasticsearchservice.dto.AuthenticationRequestDTO;
import com.rs.elasticsearchservice.dto.RedirectResponseDTO;
import com.rs.elasticsearchservice.dto.TokenResponseDTO;
import com.rs.elasticsearchservice.service.AuthenticationService;
import com.rs.elasticsearchservice.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil tokenUtil;

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<RedirectResponseDTO> authenticate(HttpServletRequest request, @Valid @RequestBody AuthenticationRequestDTO authenticationRequestDTO) throws Exception {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtSecurity = tokenUtil.generateJWTSecurity();
        var token = tokenUtil.generateToken(authentication, jwtSecurity);
        var redirectUrl = authenticationService.getRedirectUrl(token);
        return new ResponseEntity<>(new RedirectResponseDTO(redirectUrl), HttpStatus.OK);
    }

    @GetMapping("/{fetchToken}")
    public TokenResponseDTO getTokenFromFetchToken(@PathVariable String fetchToken) throws Exception {
        var realToken = authenticationService.fetchToken(fetchToken);
        return new TokenResponseDTO(realToken);
    }

}
