package account.service.repository;

import account.service.domian.Account;
import account.service.exception.BankIdAlreadyRegisteredException;
import account.service.port.IExternalService;

import java.util.HashMap;

public class AccountRepositoryAdapter implements IExternalService {
    IAccountRepository repo = new AccountRepository();

    @Override
    public void registerUser(Account acc, String userId) throws BankIdAlreadyRegisteredException {
        try {
            repo.addNewUser(acc, userId);
        } catch (IllegalArgumentException e) {
            throw new BankIdAlreadyRegisteredException(e.getMessage());
        }
    }

    @Override
    public Account getSpecificUser(String userId) {
        return repo.getSpecificUser(userId);
    }

    @Override
    public HashMap<String, Account> getUserList() {
        return repo.getUserList();
    }

}
