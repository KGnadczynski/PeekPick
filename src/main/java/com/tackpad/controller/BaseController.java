package com.tackpad.controller;

import com.tackpad.responses.BadRequestResponse;
import com.tackpad.responses.enums.BadRequestResponseType;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * Kontroller bazowy
 */
public abstract class BaseController {

    /** Logger.*/
    private static final Logger logger = Logger.getLogger(BaseController.class.getName());

    /**
     * Zwraca prawidłową odpowiedź.
     *
     * @return {@link ResponseEntity}
     */
    public ResponseEntity success() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Zwraca prawidłową odpowiedź.
     *
     * @param obj obiekt
     * @return {@link ResponseEntity}
     */
    public ResponseEntity success(Object obj) {

        logger.info("Response: {}: " + obj);
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

    /**
     * Zwraca błądy request
     *
     * @param obj Object
     * @return {@link ResponseEntity}
     */
    public ResponseEntity badRequest(Object obj) {

        logger.info("Response: {}: " + obj);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
    }

    /**
     * Zwraca błądy request
     *
     * @param badRequestResponseType type
     * @return {@link ResponseEntity}
     */
    public ResponseEntity badRequest(BadRequestResponseType badRequestResponseType ) {

        BadRequestResponse badRequestResponse = new BadRequestResponse();
        badRequestResponse.type = badRequestResponseType;

        logger.info("Response: {}: " + badRequestResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
    }

}
