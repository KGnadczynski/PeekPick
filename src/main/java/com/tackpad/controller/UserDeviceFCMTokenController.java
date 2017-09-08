package com.tackpad.controller;

import com.tackpad.models.*;
import com.tackpad.models.enums.DeviceType;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.UserDeviceFCMTokenService;
import com.tackpad.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userdevicefcmtoken")
public class UserDeviceFCMTokenController extends BaseController  {

    @Autowired
    public UserDeviceFCMTokenService userDeviceFCMTokenService;

    @Autowired
    public UserService userService;

    @PostMapping
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = UserDeviceFCMToken.class))
    ResponseEntity create(Authentication authentication,
                          @Validated(UserDeviceFCMToken.UserDeviceFCMTokenValidation.class)
                          @RequestBody UserDeviceFCMToken userDeviceFCMToken, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        if (userService.findById(userDeviceFCMToken.getUser().getId()) == null || !user.getId().equals(userDeviceFCMToken.getUser().getId())) {
            return badRequest(BadRequestResponseType.INVALID_USER_ID);
        }

        UserDeviceFCMToken deviceFCMToken = userDeviceFCMTokenService.getByUserIdAndDeviceType(userDeviceFCMToken.getUser().getId(), userDeviceFCMToken.getDeviceType());
        if (deviceFCMToken != null) {
            userDeviceFCMTokenService.delete(deviceFCMToken);
        }

        userDeviceFCMTokenService.save(userDeviceFCMToken);

        return success(userDeviceFCMToken);
    }

    @DeleteMapping("/devicetype/{devicetype}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = UserDeviceFCMToken.class))
    ResponseEntity remove(Authentication authentication, @PathVariable("devicetype") String devicetype) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        UserDeviceFCMToken userDeviceFCMToken = userDeviceFCMTokenService.getByUserIdAndDeviceType(user.getId(), DeviceType.valueOf(devicetype));

        if (userDeviceFCMToken == null) {
            return success(BadRequestResponseType.TOKEN_NOT_FOUND);
        }

        userDeviceFCMTokenService.delete(userDeviceFCMToken);

        return success(userDeviceFCMToken);
    }
}
