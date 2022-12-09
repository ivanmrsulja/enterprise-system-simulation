package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.annotation.Log;
import rs.enterprise.paymentserviceprovider.dto.SetPaymentMethodsRequestDTO;
import rs.enterprise.paymentserviceprovider.service.PaymentMethodService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @Log(message = "Getting all available payment methods.")
    @GetMapping("/all")
    public Set<String> getAllPaymentMethods(HttpServletRequest request) {
        return paymentMethodService.getAllAvailablePaymentMethods();
    }

    @Log(message = "Getting all payment methods for merchant.")
    @GetMapping("/{merchantId}/all")
    public Set<String> getAllPaymentMethodsForMerchant(HttpServletRequest request, @PathVariable String merchantId) {
        return paymentMethodService.getAllAvailablePaymentMethodsForMerchant(merchantId);
    }

    @Log(message = "Setting new payment methods for merchant.")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setPaymentMethodsForMerchant(HttpServletRequest request, @RequestBody SetPaymentMethodsRequestDTO requestDTO) {
        paymentMethodService.setPaymentMethodsForMerchant(requestDTO.getId(), requestDTO.getPaymentMethods());
    }
}
