package account.service.repository;

import account.service.domian.Account;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.HashSet;

public class AccountRepository implements IAccountRepository {

    @Getter @Setter HashMap<String, Account> userList = new HashMap<>();
    @Getter @Setter HashSet<String> bankIds = new HashSet<>();

    @Override
    public void addNewUser(Account account, String userId) throws IllegalArgumentException {
        if(bankIds.contains(account.getBankID()))
            throw new IllegalArgumentException("BankIdNotFound");
        else {
            bankIds.add(account.getBankID());
            userList.put(userId, account);
        }
    }
    @Override
    public Account getSpecificUser(String userId) throws NotFoundException {
        if(userList.containsKey(userId))
            return userList.get(userId);
        else
            throw new NotFoundException("UserNotFound");
    }
}
