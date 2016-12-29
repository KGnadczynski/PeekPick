package com.tackpad.controller;


import com.tackpad.converters.LongListConverter;
import com.tackpad.converters.MessageTypeListConverter;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.Message;
import com.tackpad.models.MessageImage;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.text.ParseException;

/**
 * 
 */
@RestController
@RequestMapping("/messages")
public class MessageController extends BaseController {

    @Autowired
    public MessageService messageService;

    @Autowired
    public CompanyBranchService companyBranchService;

    @Autowired
    public ImageStoreService imageStoreService;

    @Autowired
    public MessageImageService messageImageService;

    @Autowired
    public UserService userService;

    /** Poviera strone wiadomosci.
     *
     * @param page - strona numer
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/page/{page}")
    ResponseEntity getPage(@PathVariable("page") int page,
                           @QueryParam("pageSize") Integer pageSize,
                           @QueryParam("messageIdList") String messageIdList,
                           @QueryParam("companyBranchId") Long companyBranchId,
                           @QueryParam("companyId") Long companyId,
                           @QueryParam("companyCategoryIdList") String companyCategoryIdList,
                           @QueryParam("companyCategoryMainIdList") String companyCategoryMainIdList,
                           @QueryParam("messageTypeList") String messageTypeList,
                           @QueryParam("searchTerm") String searchTerm,
                           @QueryParam("latitude") Double latitude,
                           @QueryParam("longitude") Double longitude,
                           @QueryParam("range") Integer range,
                           @QueryParam("sortType") String sortType) {

        Page<Message> messagePage = null;

        LongListConverter longListConverter = new LongListConverter();
        MessageTypeListConverter messageTypeListConverter = new MessageTypeListConverter();
        ListingSortType listingSortType = ListingSortType.convertFromString(sortType);

        try {
            messagePage = messageService.getPage(page, pageSize, longListConverter.convert(messageIdList), companyBranchId, companyId, longListConverter.convert(companyCategoryMainIdList),
                    longListConverter.convert(companyCategoryIdList), messageTypeListConverter.convert(messageTypeList),
                    latitude, longitude, range, searchTerm, listingSortType);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Message message : messagePage.objectList) {
            MessageImage messageImage = messageImageService.getByMessageId(message.id);

            if (messageImage != null) {
                message.mainImageUrl = messageImage.imageUrl;
            }
        }

        return success(messagePage);
    }

    /**
     * Tworzy wiadomość.
     * @param message - wiadomość
     * @return @{link ResponseEntity}
     */
    @PostMapping
    ResponseEntity create(@Validated(Message.CreateMessageValidation.class)
                                                   @RequestBody Message message, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        CompanyBranch companyBranch = companyBranchService.getById(message.companyBranch.id);

        if (companyBranch == null) {
            return badRequest(BadRequestResponseType.INVALID_COMPANY_BRUNCH_ID);
        }

        messageService.save(message);
        return success(message);
    }

}
