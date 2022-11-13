package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.enterprise.paymentserviceprovider.dto.AuthenticationRequestDTO;
import rs.enterprise.paymentserviceprovider.dto.AuthenticationResponseDTO;
import rs.enterprise.paymentserviceprovider.dto.RegisterDTO;
import rs.enterprise.paymentserviceprovider.dto.RegistrationResponseDTO;
import rs.enterprise.paymentserviceprovider.service.MerchantService;
import rs.enterprise.paymentserviceprovider.util.jwt.JwtUtil;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/users")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil tokenUtil;
    private final MerchantService merchantService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil tokenUtil, MerchantService merchantService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
        this.merchantService = merchantService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(HttpServletRequest request, @RequestBody AuthenticationRequestDTO authenticationRequestDTO, HttpServletResponse response) throws AccountLockedException {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getMerchantId(), authenticationRequestDTO.getMerchantPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtSecurity = tokenUtil.generateJWTSecurity();
        String token = tokenUtil.generateToken(authentication, jwtSecurity);
        return new ResponseEntity<>(new AuthenticationResponseDTO(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public RegistrationResponseDTO register(HttpServletRequest request, @RequestBody RegisterDTO registrationRequest) {
        var newUser = merchantService.register(registrationRequest);
        return new RegistrationResponseDTO(newUser.getMerchantId(), newUser.getApiKey(), newUser.getName());
    }
}
