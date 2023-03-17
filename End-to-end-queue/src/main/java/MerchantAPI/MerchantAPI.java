package MerchantAPI;

import domain.AccountDTO;
import domain.MerchantReportList;
import domain.Payment;
import dtu.ws.fastmoney.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.HashMap;

public class MerchantAPI {
    Client client = ClientBuilder.newClient();
    String bankID;

    public String getDTUPayID() {
        return DTUPayID;
    }

    public void setDTUPayID(String DTUPayID) {
        this.DTUPayID = DTUPayID;
    }

    String DTUPayID;
    HashMap<String, AccountDTO> merchantList = new HashMap<>();
    BankService bank = new BankServiceService().getBankServicePort();


    public String registerMerchant(AccountDTO user){
        WebTarget target = client.target("http://localhost:9090/merchant/account");
        String result;
        try {
            result = target.request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_PLAIN_TYPE)
                    .post(Entity.json(user), String.class);

            String registerID = result;
            //System.out.println(result);
            merchantList.put(result, user);
            setDTUPayID(registerID);
            return registerID;
        }catch (Exception exception) {
            return "404";
        }
    }
    public HashMap<String, AccountDTO> getMerchantList() {
        return merchantList;
    }


   MerchantReportList reports = new MerchantReportList();

    //Doesnt take any id yet
    public MerchantReportList requestMerchantReports(String merchantID) {
        WebTarget target = client.target("http://localhost:9090/merchant/reports/" + merchantID);
        try {
            reports = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(MerchantReportList.class);
            return reports;
        } catch (NotFoundException exception) {
            //How does we handle exceptions here? HTTP or Custom exceptions?
            throw new NotFoundException("Reports doesn't exist");
        }
    }
    public Payment payment(Payment payment) {
        
        WebTarget target = client.target("http://localhost:9090/merchant/payment");

        try {
            Payment result = target.request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.json(payment), Payment.class);
            //System.out.println(result);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        
    }
}
