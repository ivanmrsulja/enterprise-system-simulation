package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.dto.SetPaymentMethodsRequestDTO;
import rs.enterprise.paymentserviceprovider.service.PaymentMethodService;

import java.util.Set;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping("/all")
    public Set<String> getAllPaymentMethods() {
        return paymentMethodService.getAllAvailablePaymentMethods();
    }

    @GetMapping("/{merchantId}/all")
    public Set<String> getAllPaymentMethodsForMerchant(@PathVariable String merchantId) {
        return paymentMethodService.getAllAvailablePaymentMethodsForMerchant(merchantId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setPaymentMethodsForMerchant(@RequestBody SetPaymentMethodsRequestDTO requestDTO) {
        paymentMethodService.setPaymentMethodsForMerchant(requestDTO.getId(), requestDTO.getPaymentMethods());
    }
}
