import CustomerAPI.CustomerAPI;
import MerchantAPI.MerchantAPI;
import bank.BankManager;
import domain.AccountDTO;
import domain.Payment;
import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaymentSteps {
    CustomerAPI customerAPI = new CustomerAPI();
    MerchantAPI merchantAPI = new MerchantAPI();
    String customerBankID, merchantBankID, token, merchantID;
    AccountDTO customer,merchant ;
    String customerFirstName = "csaba";
    String customerLastName = "ilyes";
    String customerCpr = "1234567890";
    String merchantFirstName = "Sulaiman";
    String merchantLastName = "kasas";
    String merchantCpr = "5456454545";


    @Given("customer with a bank account with balance {double}")
    public void customerWithABankAccountWithBalance(double balance) {
        try {
            System.out.println("\n----------------- \n"+ "Create a Customer bank id\n" + "-----------------");
            customerBankID = BankManager.createAccount(customerFirstName, customerLastName, customerCpr, BigDecimal.valueOf(balance));
            System.out.println(customerFirstName + " get an customer BankID  " + customerBankID);
        } catch (BankServiceException_Exception e) {
            System.out.println("error on customer bank account creating: " + e.getMessage());
        }

    }

    @And("that the customer is registered with DTU Pay")
    public void thatTheCustomerIsRegisteredWithDTUPay() {
        customer = new AccountDTO(customerFirstName, customerLastName,customerCpr , customerBankID);
        customerAPI.registerCustomer(customer);
        System.out.println( customerFirstName + " has been register in DTU PAY " + customerAPI.getDTUPayID());
    }

    @And("the customer requests new tokens")
    public void theCustomerRequestsNewTokens() {
        System.out.println("\n----------------- \n"+ "Show the tokens that the customer has gotten\n" + "-----------------");
        customerAPI.requestTokens(customerAPI.getDTUPayID());
        token = customerAPI.getTokens().get(0);
        System.out.println( customerFirstName + " get token: " +token);
    }

    @Given("a merchant with a bank account with balance {double}")
    public void aMerchantWithABankAccountWithBalance( double balance) {
       try {
           System.out.println("\n----------------- \n"+ "Create a merchant bank id\n" + "-----------------");
           merchantBankID = BankManager.createAccount(merchantFirstName, merchantLastName, merchantCpr, BigDecimal.valueOf(balance));
           System.out.println(merchantFirstName + " get an merchant BankID  " + merchantBankID);

        } catch (BankServiceException_Exception bankServiceException_exception){
            //If it catches an exception then this step fails
            fail(bankServiceException_exception.getMessage());
        }
    }

    @And("that the merchant is registered with DTU Pay")
    public void thatTheMerchantIsRegisteredWithDTUPay() {
        merchant = new AccountDTO(merchantFirstName, merchantLastName, merchantCpr, merchantBankID);
        merchantID = merchantAPI.registerMerchant(merchant);
        System.out.println(merchantFirstName + " has been register in DTU PAY " + merchantAPI.getDTUPayID());
        System.out.println("\n----------------- \n"+ "BankID for customer and merchant\n" + "-----------------");
        System.out.println("Customer bank id: " +customerBankID);
        System.out.println("Merchant bank id: " +merchantBankID);
    }




    @When("the merchant initiates a payment for {int} kr by the customer")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int amount) {
        try {
            Payment payment = merchantAPI.payment(new Payment(customerBankID, merchantBankID, merchantID,new BigDecimal(amount),"Cannot be Empty",token));
            assertTrue(payment instanceof Payment && payment != null);
            System.out.println("\n----------------- \n"+ "The merchant initiates a payment\n" + "-----------------");
            System.out.println(payment);
        } catch (Exception e) {
            fail();
        }

    }

    @Then("the payment is {string}")
    public void thePaymentIs(String arg0) {
    }

    @And("the balance of the customer at the bank is {double} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(double balance) throws BankServiceException_Exception {
        BigDecimal accountBalance = BankManager.getAccount(customerBankID).getBalance();
        assertEquals(balance, accountBalance.doubleValue(),0);
    }

    @And("the balance of the merchant at the bank is {double} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(double balance) throws BankServiceException_Exception {
        BigDecimal accountBalance = BankManager.getAccount(merchantBankID).getBalance();
        assertEquals(balance, accountBalance.doubleValue(), 0);
    }

    @After("@payment")
    public void cleanup() {
        System.out.println("\n----------------- \n"+ "cleanup\n" + "-----------------");
        try {
            BankManager.retireAccount(customerBankID);
            BankManager.retireAccount(merchantBankID);
            customerBankID = null;
            merchantBankID = null;
            System.out.println("cleaning done");
        } catch (BankServiceException_Exception e) {
            System.out.println("error cleaning up: " + e.getMessage());
        }
    }
}
