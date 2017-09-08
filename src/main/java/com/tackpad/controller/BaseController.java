package com.tackpad.controller;

import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.oauth2.UserRole;
import com.tackpad.responses.BadRequestResponse;
import com.tackpad.responses.enums.BadRequestResponseType;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;


/**
 * Kontroller bazowy
 */
public abstract class BaseController {

    /** Logger.*/
    protected static final Logger logger = Logger.getLogger(BaseController.class.getName());

    /**
     * Zwraca prawidłową odpowiedź.
     *
     * @return {@link ResponseEntity}
     */
    public ResponseEntity success() {

        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    /**
     * Zwraca prawidłową odpowiedź.
     *
     * @param obj obiekt
     * @return {@link ResponseEntity}
     */
    public ResponseEntity success(Object obj) {
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body(obj);
        logger.info("Response: {}: " + (String)  responseEntity.getBody().toString());
        return responseEntity;
    }

    /**
     * Zwraca błądy request
     *
     * @param obj Object
     * @return {@link ResponseEntity}
     */
    public ResponseEntity badRequest(Object obj) {
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
        logger.info("Response: {}: " + (String)  responseEntity.getBody().toString());
        return responseEntity;
    }

    /**
     * Zwraca błądy request
     *
     * @param badRequestResponseType type
     * @return {@link ResponseEntity}
     */
    public ResponseEntity badRequest(BadRequestResponseType badRequestResponseType ) {

        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.error = badRequestResponseType;
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        logger.info("Response: {}: " + (String) responseEntity.getBody());
        return responseEntity;
    }

    public ResponseEntity forbidden(Object obj) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(obj);
    }

    public boolean hasRole(UserRoleType role) {
        Collection<UserRole> authorities = (Collection<UserRole>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (UserRole authority : authorities) {
            hasRole = authority.getAuthority().equals(role.name());
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }
}
