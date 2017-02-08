package com.tackpad.services;


import com.tackpad.dao.TokenDao;
import com.tackpad.models.Token;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.oauth2.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Message service.
 * @author Przemysaw Zynis
 */
@Component
public class TokenService extends BaseService {

    @Autowired
    public TokenDao tokenDao;

    public Token getByValue(String value) {

        return tokenDao.findByValue(value);
    }


    /** Save.*/
    public void save(Token token) {
        tokenDao.save(token);
    }

    public String createConfirmAccountToken(User user) {
        Token token = new Token();
        token.tokenType = TokenType.COMPLETE_REGISTER;
        token.user = user;
        token.value = UUID.randomUUID().toString();
        tokenDao.save(token);

        return token.value;
    }

    public String createChangeEmailToken(User user, String email) {
        Token token = new Token();
        token.tokenType = TokenType.COMPLETE_REGISTER;
        token.user = user;
        token.data = email;
        token.value = UUID.randomUUID().toString();
        tokenDao.save(token);

        return token.value;
    }

    public void delete(Token token) {
        tokenDao.delete(token);
    }
}
