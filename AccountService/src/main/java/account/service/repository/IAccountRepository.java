package account.service.repository;

import account.service.domian.Account;

import javax.ws.rs.NotFoundException;
import java.util.HashMap;

public interface IAccountRepository {
    void addNewUser(Account account, String userId) throws IllegalArgumentException;
    Account getSpecificUser(String userId) throws NotFoundException;
    HashMap<String, Account> getUserList();
}
