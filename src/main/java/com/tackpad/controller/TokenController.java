package com.tackpad.controller;

import com.tackpad.models.Token;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.enums.UserStatus;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Przemysław Żynis on 02.12.2016.
 */
@Controller
@RequestMapping("/tokens")
public class TokenController extends BaseController {

    @Autowired
    public TokenService tokenService;

    @Autowired
    public UserService userService;

    @GetMapping("/value/{value}")
    ResponseEntity token(@PathVariable("value") String value) {

        Token token = tokenService.getByValue(value);

        if (token == null) {
            return badRequest(BadRequestResponseType.INVALID_TOKEN);
        }

        switch (token.getTokenType()) {

            case COMPLETE_REGISTER:
                token.getUser().setStatus(UserStatus.ACTIVE);
                userService.merge(token.getUser());
                break;
            case CHANGE_EMAIL:
                token.getUser().setEmail(token.getData());
                userService.merge(token.getUser());
                break;

            default:
                throw new UnsupportedOperationException("Wrong token type: {} " + token.getTokenType());
        }

        tokenService.delete(token);
        return success("Aktywowano");
    }


    @InitBinder("createBusinessUserForm")
    void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}
