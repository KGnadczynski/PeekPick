package com.tackpad.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tackpad.converters.LongListConverter;
import com.tackpad.converters.MessageTypeListConverter;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.Message;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.CompanyBranchService;
import com.tackpad.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "great-software",
            "api_key", "663115442842357",
            "api_secret", "Yacqx7ZfBisrLjs_QfqQ2gzV3kE"));


    @Autowired
    public MessageService messageService;

    @Autowired
    public CompanyBranchService companyBranchService;

    /** Poviera strone wiadomosci.
     *
     * @param page - strona numer
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/page/{page}")
    ResponseEntity getPage(@AuthenticationPrincipal User user, @PathVariable("page") int page,
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
        return success();
    }

    @PostMapping(value = "/upload")
    public String uploadPhoto(@RequestPart("file") MultipartFile file) throws IOException {

       /* SingletonManager manager = new SingletonManager();
        manager.setCloudinary(cloudinary);
        manager.init();

        Map uploadResult = cloudinary.uploader().upload(photoUpload.getFile().getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        *//*PhotoUploadValidator validator = new PhotoUploadValidator();
        validator.validate(photoUpload, result);

        Map uploadResult = null;
        if (photoUpload.getFile() != null && !photoUpload.getFile().isEmpty()) {
            uploadResult = Singleton.getCloudinary().uploader().upload(photoUpload.getFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            photoUpload.setPublicId((String) uploadResult.get("public_id"));
            Object version = uploadResult.get("version");
            if (version instanceof Integer) {
                photoUpload.setVersion(new Long((Integer) version));
            } else {
                photoUpload.setVersion((Long) version);
            }

            photoUpload.setSignature((String) uploadResult.get("signature"));
            photoUpload.setFormat((String) uploadResult.get("format"));
            photoUpload.setResourceType((String) uploadResult.get("resource_type"));
        }*/

        return "upload";
    }

}
