package com.tackpad.controller;


import com.tackpad.models.Company;
import com.tackpad.models.Message;
import com.tackpad.models.Image;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.*;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
 * 
 */
@RestController
@RequestMapping("/messageimages")
public class MessageImageController extends BaseController {

    @Autowired
    public MessageService messageService;

    @Autowired
    public UserService userService;

    @Autowired
    public ImageStoreService imageStoreService;

    @Autowired
    public MessageImageService messageImageService;

    @RequestMapping(value = "/messageId/{messageId}", method = RequestMethod.POST)
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Image.class))
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            Authentication authentication,
            @PathVariable("messageId") Long messageId,
            @RequestParam("file") MultipartFile multipartFile) {

        Message message = messageService.getById(messageId);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());


        if (message == null || !message.getUser().getCompany().getId().equals(user.getCompany().getId())) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        deleteMessageImage(authentication, message.getId());

        try {
            Image image = imageStoreService.uploadMessagePhoto(multipartFile.getBytes());
            image.setMessage(message);

            messageImageService.save(image);
            return success(image);

        } catch (ParseException | IOException e) {
            return badRequest(BadRequestResponseType.UPLOAD_IMAGE_FAIL);
        }

    }

    @DeleteMapping(value = "/messageId/{messageId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Message.class))
    ResponseEntity deleteMessageImage(Authentication authentication, @PathVariable("messageId") Long messageId) {

        Message message = messageService.getById(messageId);

        if (message == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        if (!message.getUser().getId().equals(user.getId())) {
            return forbidden(BadRequestResponseType.INVALID_ID);
        }

        Image image = messageImageService.getByMessageId(message.getId());
        if (image == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        messageImageService.delete(image);

        return success(message);
    }


}
