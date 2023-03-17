package account.service.port;

import account.service.domian.Account;
import account.service.exception.BankIdAlreadyRegisteredException;
import account.service.exception.UserNotFoundException;

import java.util.HashMap;

public interface IExternalService {
    void registerUser(Account acc, String userId) throws BankIdAlreadyRegisteredException;
    Account getSpecificUser(String userId) throws UserNotFoundException;
    HashMap<String, Account> getUserList();
}
