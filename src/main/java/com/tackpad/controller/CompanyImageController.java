package com.tackpad.controller;


import com.tackpad.models.Company;
import com.tackpad.models.Image;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.Page;
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

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

/**
 * 
 */
@RestController
@RequestMapping("/companyimages")
public class CompanyImageController extends BaseController {

    @Autowired
    public CompanyService companyService;

    @Autowired
    public UserService userService;

    @Autowired
    public ImageStoreService imageStoreService;

    @Autowired
    public MessageImageService messageImageService;

    @RequestMapping(value = "/companyId/{companyId}", method = RequestMethod.POST)
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Image.class))
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            Authentication authentication,
            @PathVariable("companyId") Long companyId,
            @RequestParam("file") MultipartFile multipartFile) {

        Company company = companyService.getById(companyId);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        if (!Objects.equals(user.getCompany().getId(), companyId)) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        try {
            Image image = messageImageService.getByCompanyId(companyId);
            if (image != null) {
                messageImageService.delete(image);
            }

            image = imageStoreService.uploadMessagePhoto(multipartFile.getBytes());
            image.setCompany(company);

            messageImageService.save(image);
            return success(image);

        } catch (ParseException | IOException e) {
            return badRequest(BadRequestResponseType.UPLOAD_IMAGE_FAIL);
        }

    }

    @GetMapping(value = "/companyId/{companyId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Image.class))
    ResponseEntity getCompanyLogo(@PathVariable("companyId") Long companyId) {

        Image image = messageImageService.getByCompanyId(companyId);
        if (image == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        return success(image);
    }


}
