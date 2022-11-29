package rs.enterprise.paymentserviceprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.enterprise.paymentserviceprovider.clients.DummyClient;
import rs.enterprise.paymentserviceprovider.dto.DummyDTO;
import rs.enterprise.paymentserviceprovider.model.Payment;
import rs.enterprise.paymentserviceprovider.service.PaymentInterface;
import rs.enterprise.paymentserviceprovider.spi.PaymentServiceFinder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/dummy")
public class DummyController {

    private final DummyClient dummyClient;

    private final PaymentServiceFinder paymentServiceFinder;

    @Autowired
    public DummyController(DummyClient dummyClient, PaymentServiceFinder paymentServiceFinder) {
        this.dummyClient = dummyClient;
        this.paymentServiceFinder = paymentServiceFinder;
    }

    @GetMapping("/all")
    public ArrayList<DummyDTO> returnDummyData() {
        return new ArrayList<>(
                List.of(
                    new DummyDTO[]{new DummyDTO("text1", 1),
                    new DummyDTO("text2", 2)}));
    }

    @GetMapping("/fetch")
    public DummyDTO returnFetchedDummyData() {
        return dummyClient.getData();
    }

    @GetMapping("/payments")
    public List<Payment> getPayments(@RequestParam String paymentServiceName) {
        var payments = new ArrayList<Payment>();
        paymentServiceFinder.providers(true).forEachRemaining(provider -> {
            PaymentInterface paymentMethod = provider.create();

//            if (paymentMethod.getPaymentServiceName().equals(paymentServiceName)) {
//                payments.addAll(paymentMethod.getPayments());
//            }
        });

        return payments;
    }

}
