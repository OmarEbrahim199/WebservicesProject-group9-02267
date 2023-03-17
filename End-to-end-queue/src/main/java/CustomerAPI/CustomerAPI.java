package CustomerAPI;


import domain.AccountDTO;
import domain.CustomerReportList;
import domain.TokenList;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerAPI {
    private ArrayList<String> tokens = new ArrayList<>();
    private ArrayList<String> used = new ArrayList<>();
    HashMap<String, AccountDTO> customerList = new HashMap<>();
    Client client = ClientBuilder.newClient();
    String bankID;
    String DTUPayID;

    public String requestTokens(String userID){
        WebTarget target = client.target("http://localhost:9090/customer/tokens");
        try {
            tokens = target.request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.json(userID), TokenList.class).getTokens();
            System.out.println("TokenList: " + tokens);
            return "Tokens received";
        }catch (NotFoundException exception) {
            //How does we handle exceptions here? HTTP or Custom exceptions?
            return "Tokens not received";
        }
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<String> getUsed() {
        return used;
    }

    public void setUsed(ArrayList<String> used) {
        this.used = used;
    }

    public String registerCustomer(AccountDTO user) {
        WebTarget target = client.target("http://localhost:9090/customer/account");
        String result;
        try {
            result = target.request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_PLAIN_TYPE)
                    .post(Entity.json(user), String.class);

            String registerID = result;
            //System.out.println(result);
            customerList.put(result, user);
            setDTUPayID(registerID);
            return registerID;

        }catch (Exception exception) {
            return "404";
        }
    }


    CustomerReportList reports = new CustomerReportList();

   public CustomerReportList requestCustomerReports(String customerID) {
        WebTarget target = client.target("http://localhost:9090/customer/reports/" + customerID);
        try {
            reports = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(CustomerReportList.class);
            return reports;
        } catch (NotFoundException exception) {
            //How does we handle exceptions here? HTTP or Custom exceptions?
            throw new NotFoundException("Reports doesn't exist");
        }
    }

    public HashMap<String, AccountDTO> getCustomerList() {
        return customerList;
    }

    public String getDTUPayID() {
        return DTUPayID;
    }

    public void setDTUPayID(String DTUPayID) {
        this.DTUPayID = DTUPayID;
    }
}
