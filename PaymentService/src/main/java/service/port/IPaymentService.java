package service.port;

import dtu.ws.fastmoney.BankServiceException_Exception;
import service.domain.Payment;

public interface IPaymentService {
    public void transferMoney(Payment p) throws BankServiceException_Exception;
    public void refundMoney(Payment p) throws BankServiceException_Exception;
}
