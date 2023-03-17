package service;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import service.domain.Payment;
import service.port.IPaymentService;

public class PaymentService implements IPaymentService {
    BankService bankService = new BankServiceService().getBankServicePort();


    @Override
    public void transferMoney(Payment p) throws BankServiceException_Exception {
        bankService.transferMoneyFromTo(p.getCustomerBankId(),p.getMerchantBankId(),p.getAmount(),p.getDescription());
    }

    @Override
    public void refundMoney(Payment p) throws BankServiceException_Exception {
        bankService.transferMoneyFromTo(p.getMerchantBankId(),p.getCustomerBankId(),p.getAmount(),p.getDescription());
    }
}
