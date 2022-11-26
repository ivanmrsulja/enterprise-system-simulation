package rs.enterprise.paymentserviceprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.TwoFactorAuthenticationToken;
import rs.enterprise.paymentserviceprovider.repository.MerchantRepository;
import rs.enterprise.paymentserviceprovider.repository.TwoFactorAuthenticationTokenRepository;
import rs.enterprise.paymentserviceprovider.util.EncryptionUtil;
import rs.enterprise.paymentserviceprovider.util.SecureStringGenerator;

@Service
public class TwoFactorAuthenticationService {

    private final JavaMailSender emailSender;
    private final TwoFactorAuthenticationTokenRepository twoFactorAuthenticationTokenRepository;
    private final MerchantRepository merchantRepository;

    private final SecureStringGenerator secureStringGenerator;
    private final EncryptionUtil encryptionUtil;

    @Autowired
    public TwoFactorAuthenticationService(JavaMailSender emailSender,
                                          TwoFactorAuthenticationTokenRepository twoFactorAuthenticationTokenRepository,
                                          MerchantRepository merchantRepository,
                                          SecureStringGenerator secureStringGenerator,
                                          EncryptionUtil encryptionUtil) {
        this.emailSender = emailSender;
        this.twoFactorAuthenticationTokenRepository = twoFactorAuthenticationTokenRepository;
        this.merchantRepository = merchantRepository;
        this.secureStringGenerator = secureStringGenerator;
        this.encryptionUtil = encryptionUtil;
    }

    public void createNewAuthToken(String merchantId, String token) throws Exception {
        var merchant = merchantRepository.findByMerchantId(merchantId).orElseThrow(() -> new NotFoundException("There is no merchant present with ID: " + merchantId));
        var pin = secureStringGenerator.generate(5);
        var twoFAToken = new TwoFactorAuthenticationToken(merchantId, pin, encryptionUtil.encrypt(token));
        twoFactorAuthenticationTokenRepository.save(twoFAToken);
        sendPinViaEmail(merchant.getEmail(), pin);
    }

    public String verifyToken(String merchantId, String pinCode) throws Exception {
        var twoFAToken = twoFactorAuthenticationTokenRepository.verifyToken(merchantId, pinCode)
                .orElseThrow(() -> new NotFoundException("No 2FA token present for merchant with ID: " + merchantId));
        var token = encryptionUtil.decrypt(twoFAToken.getToken());
        twoFactorAuthenticationTokenRepository.delete(twoFAToken);
        return token;
    }

    @Async
    private void sendPinViaEmail(String to, String pin) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("razminiravanje@hotmail.com");
        message.setTo(to);
        message.setSubject("Your PSP access code");
        message.setText("Hello there,\nYour access code is: " + pin + ".\n\nEnjoy using our service,\nRazminiravanje");
        emailSender.send(message);
    }
}
