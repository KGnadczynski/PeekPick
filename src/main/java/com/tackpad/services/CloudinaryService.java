package com.tackpad.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Created by Przemek on 2016-09-30.
 */
public class CloudinaryService {

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "great-software",
            "api_key", "663115442842357",
            "api_secret", "Yacqx7ZfBisrLjs_QfqQ2gzV3kE"));

    /** Upload file.*//*
    public void uploadFile() {
        File file = new File("my_image.jpg");
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    }*/
}
