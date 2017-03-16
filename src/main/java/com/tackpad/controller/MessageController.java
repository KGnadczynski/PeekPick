package com.tackpad.controller;


import com.tackpad.converters.LongListConverter;
import com.tackpad.converters.MessageTypeListConverter;
import com.tackpad.models.*;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.CountResponse;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.responses.MessagePage;
import com.tackpad.services.*;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;

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
    public CompanyService companyService;

    @Autowired
    public UserService userService;

    @Autowired
    public MessageLocationService messageLocationService;

    /** Poviera strone wiadomosci.
     *
     * @param page - strona numer
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/page/{page}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = MessagePage.class))
    ResponseEntity getPage(@PathVariable("page") int page,
                           @RequestParam(value = "pageSize", required=false) Integer pageSize,
                           @RequestParam(value = "messageIdList", required=false) String messageIdList,
                           @RequestParam(value = "companyBranchId", required=false) Long companyBranchId,
                           @RequestParam(value = "companyId", required=false) Long companyId,
                           @RequestParam(value = "companyCategoryIdList", required=false) String companyCategoryIdList,
                           @RequestParam(value = "companyCategoryMainIdList", required=false) String companyCategoryMainIdList,
                           @RequestParam(value = "messageTypeList", required=false) String messageTypeList,
                           @RequestParam(value = "searchTerm", required=false) String searchTerm,
                           @RequestParam(value = "latitude", required=false) Double latitude,
                           @RequestParam(value = "longitude", required=false) Double longitude,
                           @RequestParam(value = "range", required=false) Double range,
                           @RequestParam(value = "sortType", required=false) String sortType) {

        MessagePage messagePage = null;

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
            Image messageImage = messageImageService.getByMessageId(message.getId());

            if (messageImage != null) {
                message.setMainImageUrl(messageImage.getImageUrl());
            }

            Image companyLogoImage = messageImageService.getByCompanyId(message.getUser().getCompany().getId());

            if (companyLogoImage != null) {
                message.getUser().getCompany().setMainImageUrl(companyLogoImage.getImageUrl());
            }

            try {
                Page<CompanyBranch> companyBranchPage = companyBranchService.getPage(1, 1,
                        Collections.singletonList(message.getId()), null,  null, latitude,
                        longitude, null, null, ListingSortType.DISTANCE);

                if (!companyBranchPage.objectList.isEmpty()) {
                    message.setNearestCompanyBranch(companyBranchPage.objectList.get(0));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return success(messagePage);
    }

    @GetMapping(value = "/{messageId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Message.class))
    ResponseEntity getMessage(@PathVariable("messageId") Long messageId) {

        Message message = messageService.getById(messageId);

        if (message == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        Image image = messageImageService.getByMessageId(message.getId());

        if (image != null) {
            message.setMainImageUrl(image.getImageUrl());
        }

        return success(message);
    }

    @GetMapping(value = "/companyId/{companyId}/count")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CountResponse.class))
    ResponseEntity getCount(@PathVariable("companyId") Long companyId) {

        Company company = companyService.getById(companyId);
        if (company == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        CountResponse countResponse = new CountResponse();
        countResponse.count = messageService.getCount(companyId);
        return success(countResponse);
    }

    /**
     * Tworzy wiadomość.
     * @param message - wiadomość
     * @return @{link ResponseEntity}
     */
    @PostMapping
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Message.class))
    ResponseEntity create(Authentication authentication,
                          @Validated(Message.CreateMessageValidation.class)
                          @RequestBody Message message, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());
        MessageLocation messageLocation = message.getLocation();

        for (CompanyBranch companyBranchContainId : message.getCompanyBranchList()) {
            CompanyBranch companyBranch = companyBranchService.getById(companyBranchContainId.getId());
            if (companyBranch == null || !user.getCompany().getId().equals(companyBranch.getCompany().getId())) {
                return badRequest(BadRequestResponseType.INVALID_COMPANY_BRUNCH_ID);
            }
        }

        message.setUser(user);

        if (messageLocation != null) {
            messageLocationService.save(messageLocation);
        }

        messageService.save(message);

        return success(message);
    }

}
