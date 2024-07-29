package com.mrlee.free_board.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class MyFileUtils {

    private static final String fileDirectory = "/Users/myhome/Desktop/project/free-board/src/main/resources/static/images/";

    public static String uploadFile(MultipartFile file) {
        if (file == null) {
            return null;
        }
        int pos = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
        String ext = file.getOriginalFilename().substring(pos + 1);
        String uuid = UUID.randomUUID().toString();
        String fileFullPath = uuid + "." + ext;
        try {
            file.transferTo(new File(fileDirectory + fileFullPath));
            log.info("file upload occurs={}", fileFullPath);
            return fileFullPath;
        } catch (IOException e) {
            log.info("cause={},errorMessage = {}", e.getCause(), e.getMessage());
            throw new RuntimeException("파일 입출력 예외 발생!");
        }
    }
}
