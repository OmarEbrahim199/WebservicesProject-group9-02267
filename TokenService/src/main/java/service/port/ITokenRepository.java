package service.port;

import service.domain.TokenList;
import service.exceptions.UserNotFoundException;

import java.util.ArrayList;

public interface ITokenRepository {
    ArrayList<TokenList> getTokens();
    TokenList getTokensByUser(String userID);
    String getUserByToken(String tokenID) throws UserNotFoundException;
    TokenList createTokenList(String userID);
    TokenList updateTokenList(String userID, TokenList tokenList);
    TokenList removeTokenList(String userID);
    String consumeToken(String tokenID) throws UserNotFoundException;
}
