import CustomerAPI.CustomerAPI;
import ManagerAPI.ManagerAPI;
import MerchantAPI.MerchantAPI;
import bank.BankManager;
import domain.AccountDTO;
import domain.Payment;
import domain.Report.CustomerReport;
import domain.Report.MerchantReport;
import domain.Report.Report;
import domain.Report.ReportList;
import dtu.ws.fastmoney.BankServiceException_Exception;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ReportStep {
    ArrayList<MerchantReport> merchantReports;
    ArrayList<CustomerReport> customerReports;
    String token, customerBankID, merchantBankID, customerID, merchantID;
    ManagerAPI managerAPI = new ManagerAPI();
    MerchantAPI merchantAPI = new MerchantAPI();
    CustomerAPI customerAPI = new CustomerAPI();
    AccountDTO user1, user2;
    ReportList reportList = new ReportList();


    @Before("@report")
    public void setupAccount() throws BankServiceException_Exception, InterruptedException {

        try {
            customerBankID = BankManager.createAccount("Stefan", "Munkmand","bonkobonko", new BigDecimal(99999));
            merchantBankID = BankManager.createAccount("Shania", "Haumand","bonkobingo", new BigDecimal(0));
        } catch (BankServiceException_Exception e) {
            System.out.println("error setting up: " + e.getMessage());
        }
        user1 = new AccountDTO("Stefan", "Munkmand", "bonkobonko", customerBankID);
        user2 = new AccountDTO("Shania", "Haumand", "bonkobingo", merchantBankID);

        merchantID = merchantAPI.registerMerchant(user2);
        customerID = customerAPI.registerCustomer(user1);

        customerAPI.requestTokens(customerID);

    }
    @Given("one completed transaction")
    public void oneCompletedTransaction() {
        token = customerAPI.getTokens().get(0);
        Payment p = merchantAPI.payment(new Payment(customerBankID,merchantBankID, merchantID,new BigDecimal(10000),"Cannot be Empty",token));
        assertNull(p.getErrorMessage());
        assertNotNull(merchantID);
        assertNotNull(customerID);
    }

    @And("the manager request the reportList")
    public void theManagerRequestTheReportList() {
        reportList = managerAPI.requestManagerReports();
        assertNotNull(reportList);
    }

    @Then("the reportList should contain two reports with the same tokenID")
    public void theReportListShouldContainTwoReportsWithTheSameTokenID() {
        ArrayList<Report> manArray = reportList.getReportList();
        int increment = 0;
        for (Report report : manArray) {
            if (report.getTokenID().equals(customerAPI.getTokens().get(0)))
                increment++;
        }
        assertEquals(2, increment);
    }

    @When("the customer requests their report")
    public void theCustomerRequestsTheirReport() {
        customerReports = customerAPI.requestCustomerReports(customerID).getReportList();
        assertNotNull(customerReports);
    }

    @And("the merchant requests their report")
    public void theMerchantRequestsTheirReport() {
        merchantReports = merchantAPI.requestMerchantReports(merchantID).getReportList();
        assertNotNull(merchantReports);
    }

    @Then("the customer receives {int} report")
    public void theCustomerReceivesReport(int arg0) {
        assertEquals(1, customerReports.size());
    }

    @And("the merchant receives {int} report")
    public void theMerchantReceivesReport(int arg0) {
        assertEquals(1, merchantReports.size());
    }

    @After("@report")
    public void cleanUpReport() throws BankServiceException_Exception {
        BankManager.retireAccount(customerBankID);
        BankManager.retireAccount(merchantBankID);
    }
}
