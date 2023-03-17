package service.port;

import service.domain.TokenList;
import service.exceptions.InvalidTokenException;
import service.exceptions.ToManyTokensLeftException;
import service.exceptions.UserNotFoundException;

public interface ITokenService {
    TokenList createUser(String userID);
    TokenList requestNewSet(String userID) throws ToManyTokensLeftException, UserNotFoundException;
    String consumeToken(String token) throws InvalidTokenException, UserNotFoundException;
    TokenList getTokensFromUserID(String userID) throws UserNotFoundException;

}
