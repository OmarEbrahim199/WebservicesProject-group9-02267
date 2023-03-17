package account.service.domian;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class Account {
    String firstName;
    String lastName;
    String cpr;
    String bankID;
}
