package com.tackpad.controller;


import com.tackpad.models.Message;
import com.tackpad.models.Image;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.*;
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
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            Authentication authentication,
            @PathVariable("messageId") Long messageId,
            @RequestParam("file") MultipartFile multipartFile) {

        Message message = messageService.getById(messageId);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        if (message == null || !message.companyBranch.company.id.equals(user.getCompany().id)) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        try {
            Image image = imageStoreService.uploadMessagePhoto(multipartFile.getBytes());
            image.message = message;

            messageImageService.save(image);
            return success(image);

        } catch (ParseException | IOException e) {
            return badRequest(BadRequestResponseType.UPLOAD_IMAGE_FAIL);
        }

    }


}
