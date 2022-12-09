package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.annotation.Log;
import rs.enterprise.paymentserviceprovider.clients.AcquirerBankClient;
import rs.enterprise.paymentserviceprovider.dto.*;
import rs.enterprise.paymentserviceprovider.exception.InvalidUsernameOrPasswordException;
import rs.enterprise.paymentserviceprovider.util.jwt.JwtUtil;
import rs.enterprise.paymentserviceprovider.service.MerchantService;
import rs.enterprise.paymentserviceprovider.service.TwoFactorAuthenticationService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/users")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil tokenUtil;
    private final MerchantService merchantService;
    private final AcquirerBankClient acquirerBankClient;

    private final TwoFactorAuthenticationService twoFactorAuthenticationService;

    @Value("${API_KEY}")
    private String apiKey;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtUtil tokenUtil,
                                    MerchantService merchantService,
                                    AcquirerBankClient acquirerBankClient,
                                    TwoFactorAuthenticationService twoFactorAuthenticationService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
        this.merchantService = merchantService;
        this.acquirerBankClient = acquirerBankClient;
        this.twoFactorAuthenticationService = twoFactorAuthenticationService;
    }

    @Log(message = "Authentication attempt.")
    @PostMapping("/authenticate/first-step")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(HttpServletRequest request, @RequestBody AuthenticationRequestDTO authenticationRequestDTO) throws Exception {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getMerchantId(), authenticationRequestDTO.getMerchantPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtSecurity = tokenUtil.generateJWTSecurity();
        var token = tokenUtil.generateToken(authentication, jwtSecurity);
        twoFactorAuthenticationService.createNewAuthToken(authenticationRequestDTO.getMerchantId(), token);
        return new ResponseEntity<>(new AuthenticationResponseDTO("PATIENCE_MY_YOUNG_PADAWAN"), HttpStatus.OK);
    }

    @Log(message = "Authentication attempt 2FA.")
    @PostMapping("/authenticate/second-step")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(HttpServletRequest request, @RequestBody TwoFactorAuthenticationRequestDTO authRequest) throws Exception {
        var token = twoFactorAuthenticationService.verifyToken(authRequest.getMerchantId(), authRequest.getPinCode());
        return new ResponseEntity<>(new AuthenticationResponseDTO(token), HttpStatus.OK);
    }

    @Log(message = "Registration attempt.")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDTO register(HttpServletRequest request, @RequestBody RegisterDTO registrationRequest) {
        if (!acquirerBankClient.authenticateMerchant(apiKey, new AcquirerBankMerchantAuthenticationDTO(registrationRequest.getMerchantId(), registrationRequest.getMerchantPassword()))) {
            throw new InvalidUsernameOrPasswordException("Username and password that you have provided do not match.");
        }
        var newUser = merchantService.register(registrationRequest);
        return new RegistrationResponseDTO(newUser.getMerchantId(), newUser.getApiKey(), newUser.getName());
    }
}
