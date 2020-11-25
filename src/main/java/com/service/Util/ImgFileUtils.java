package com.service.Util;

import com.model.entity.ImgFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImgFileUtils {
    public static ImgFile createImgFile(MultipartFile file, String filename, String fileType) throws IOException {
        ImgFile img = createImgFile(file.getBytes(), fileType);
        img.setFileName(filename);
        return img;
    }

    public static ImgFile createImgFile(byte[] bytes, String fileType) throws IOException {
        ImgFile imgFile = new ImgFile();
        imgFile.setFileType(fileType);
        imgFile.setData(bytes);
        return imgFile;
    }
}
