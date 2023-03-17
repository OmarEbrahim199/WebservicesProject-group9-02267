import CustomerAPI.CustomerAPI;
import bank.BankManager;
import domain.AccountDTO;
import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTokenStep {

    CustomerAPI customerAPI = new CustomerAPI();

    String bankId, message;
    int oldCount, newCount;

    String customerFirstName = "Sulaiman";
    String customerLastName = "kasas";
    String customerCPR = "0765483451";
    @Given("a customer with a bank account with balance {double}")
    public void aCustomerWithABankAccountWithBalance(double balance) {
        System.out.println("\n----------------- \n"+ "Test request token\n" + "-----------------");
        try {
            bankId  = BankManager.createAccount(customerFirstName, customerLastName, customerCPR, BigDecimal.valueOf(balance));
            System.out.println(customerFirstName + " get a bankId " + bankId);
        }
        catch (Exception ignored){
            System.out.println(ignored.getMessage());
        }
    }

    @And("the customer is registered with DTU pay")
    public void theCustomerIsRegisteredWithDTUPay() {
        customerAPI.setDTUPayID(customerAPI.registerCustomer(new AccountDTO(customerFirstName, customerLastName, customerCPR, bankId)));
        System.out.println(customerFirstName + " registered with dtu pay" );
    }

    @And("the customer has {int} token left")
    public void theCustomerHasTokenLeft(int arg0) {
    }

    @When("the customer request new Tokens")
    public void theCustomerRequestNewTokens() {
        oldCount = customerAPI.getTokens().size();
        message = customerAPI.requestTokens(customerAPI.getDTUPayID());
        System.out.println(message +  " Successfully");
    }

    @Then("the customer receives {int} new tokens")
    public void theCustomerReceivesNewTokens(int arg0) {
        newCount = customerAPI.getTokens().size();
        assertEquals(arg0, newCount - oldCount);
    }

    @And("the customer receive the following message {string}")
    public void theCustomerReceiveTheFollowingMessage(String arg0) {
    }

    @After("@token")
    public void cleanUpAccounts() throws BankServiceException_Exception {
        BankManager.retireAccount(bankId);
    }
}
