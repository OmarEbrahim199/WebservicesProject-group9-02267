package account.service;

import account.service.domian.Account;
import account.service.exception.BankIdAlreadyRegisteredException;
import account.service.exception.BankIdNotFoundException;
import account.service.exception.InvalidRegistrationInputException;
import account.service.exception.UserNotFoundException;
import account.service.port.IExternalService;

//Engages with our AccountAdapter
public class AccountService {
    IExternalService service;

    public AccountService(IExternalService service) {
        this.service = service;
    }


    public String registerUser(Account acc, String role) throws BankIdAlreadyRegisteredException, InvalidRegistrationInputException, BankIdNotFoundException {
            String id = idGenerator.generateID(role);
            service.registerUser(acc, id);
            return id;
    }

    public Account getSpecifiedUser(String userId) throws UserNotFoundException {
        return service.getSpecificUser(userId);
    }

}
