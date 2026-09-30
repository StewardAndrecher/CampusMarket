package com.campusmarket.campusmarketserver.controller;

import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${campus.upload.dir:./data/uploads}")
    private String uploadDir;

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) throw new BusinessException("文件为空");
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }
        if (!ext.matches("\\.(jpg|jpeg|png|webp|gif|bmp)$")) {
            throw new BusinessException("只允许图片文件(jpg/png/webp/gif)");
        }
        if (file.getSize() > 5 * 1024 * 1024) throw new BusinessException("图片不能超过5MB");

        String filename = UUID.randomUUID().toString().replace("-", "") + ext;
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        File dest = new File(dir, filename);
        try {
            file.transferTo(dest.getAbsoluteFile());
        } catch (IOException e) {
            throw new BusinessException("上传失败: " + e.getMessage());
        }
        Map<String, String> m = new HashMap<>();
        m.put("url", "/uploads/" + filename);
        return Result.ok(m);
    }
}
