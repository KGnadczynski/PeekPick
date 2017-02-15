package com.tackpad.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tackpad.models.Image;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Message service.
 * @author Przemysaw Zynis
 */
@Component
public class ImageStoreService extends BaseService {

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "great-software",
            "api_key", "663115442842357",
            "api_secret", "Yacqx7ZfBisrLjs_QfqQ2gzV3kE"));

    public Image uploadMessagePhoto(byte[] file) throws IOException, ParseException {

        Map responseMap = upload(file);

        Image image = new Image();
        image.setImageId((String) responseMap.get("public_id"));
        image.setImageUrl((String) responseMap.get("secure_url"));

        return image;
    }


    private Map upload(byte[] file) throws ParseException, IOException {
        return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    }

}
