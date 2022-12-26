package rs.enterprise.paymentserviceprovider.unit;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import rs.enterprise.paymentserviceprovider.dto.AcquirerBankPaymentRequestDTO;
import rs.enterprise.paymentserviceprovider.exception.NotFoundException;
import rs.enterprise.paymentserviceprovider.model.BankPayment;
import rs.enterprise.paymentserviceprovider.model.enums.TransactionState;
import rs.enterprise.paymentserviceprovider.repository.BankPaymentRepository;
import rs.enterprise.paymentserviceprovider.service.BankPaymentService;
import rs.enterprise.paymentserviceprovider.util.EncryptionUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BankPaymentServiceTest {

    @Mock
    private BankPaymentRepository bankPaymentRepository;

    @Mock
    private EncryptionUtil encryptionUtil;

    @InjectMocks
    private BankPaymentService service;

    @Test
    void createNewPaymentAndGenerateRedirectUrl_calledWithValidParams_isSuccess() throws Exception {
        // GIVEN
        var paymentRequest = new AcquirerBankPaymentRequestDTO("id",
                "password",
                200.0,
                1L,
                "timestamp",
                "url1", "url2", "url3");
        var payment = new BankPayment();
        payment.setMerchantId("ID");
        payment.setMerchantOrderId(123L);
        payment.setId(1);
        doReturn(payment).when(bankPaymentRepository).save(any());
        doReturn("aaa").when(encryptionUtil).encrypt(any());

        // WHEN
        var url = service.createNewPaymentAndGenerateRedirectUrl(paymentRequest);

        // THEN
        verify(bankPaymentRepository, times(1)).save(any());
        assertTrue(url.contains("/make-payment/"));
    }

    @Test
    void fetchBankPaymentRequest_calledWithValidParams_isSuccess() throws Exception {
        // GIVEN
        var payment = new BankPayment("id",
                "password",
                123L,
                "timestamp",
                200.00,
                "url1", "url2", "url3");

        doReturn(Optional.of(payment)).when(bankPaymentRepository).getByIdAndMerchantOrderId(1, 1L);

        // WHEN
        var paymentRequest = service.fetchBankPaymentRequest(1L, 1);

        // THEN
        assertEquals(payment.getMerchantOrderId(), paymentRequest.getMerchantOrderId());
        assertEquals(payment.getAmount(), paymentRequest.getAmount());
    }

    @Test
    void fetchBankPaymentRequest_calledWithInvalidParams_isSuccess() throws Exception {
        // GIVEN
        doThrow(NotFoundException.class).when(bankPaymentRepository).getByIdAndMerchantOrderId(1, 1L);

        // THEN
        assertThrows(NotFoundException.class, () -> service.fetchBankPaymentRequest(1L, 1));
    }

    @Test
    void handleFinalRedirect_calledWithStateSuccess_returnsSuccessUrl() {
        var payment = new BankPayment("id",
                "password",
                123L,
                "timestamp",
                200.00,
                "success", "failed", "error");

        doReturn(Optional.of(payment)).when(bankPaymentRepository).getByMerchantOrderId(1L);

        // WHEN
        var responseUrl = service.handleFinalRedirect(TransactionState.SUCCESS, 1L);

        // THEN
        assertEquals(payment.getSuccessUrl(), responseUrl);
    }

    @Test
    void handleFinalRedirect_calledWithStateFailed_returnsFailedUrl() {
        var payment = new BankPayment("id",
                "password",
                123L,
                "timestamp",
                200.00,
                "success", "failed", "error");

        doReturn(Optional.of(payment)).when(bankPaymentRepository).getByMerchantOrderId(1L);

        // WHEN
        var responseUrl = service.handleFinalRedirect(TransactionState.FAILED, 1L);

        // THEN
        assertEquals(payment.getFailedUrl(), responseUrl);
    }

    @Test
    void handleFinalRedirect_calledWithErrorSuccess_returnsErrorUrl() {
        // GIVEN
        var payment = new BankPayment("id",
                "password",
                123L,
                "timestamp",
                200.00,
                "success", "failed", "error");

        doReturn(Optional.of(payment)).when(bankPaymentRepository).getByMerchantOrderId(1L);

        // WHEN
        var responseUrl = service.handleFinalRedirect(TransactionState.ERROR, 1L);

        // THEN
        assertEquals(payment.getErrorUrl(), responseUrl);
    }

    @Test
    void handleFinalRedirect_calledWithInvalidId_throwsException() {
        // GIVEN
        doThrow(NotFoundException.class).when(bankPaymentRepository).getByMerchantOrderId(1L);

        // THEN
        assertThrows(NotFoundException.class, () -> service.handleFinalRedirect(TransactionState.ERROR, 1L));
    }
}
