package com.leyou.upload.web;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jhmarryme.cn
 * @date 2019/5/17 20:15
 */

@RestController
@RequestMapping("upload")
public class LyUploadcontroller {


    @Autowired
    private UploadService uploadService;
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){

        String url = uploadService.upload(file);

        //判断结果
        if (StringUtils.isBlank(url)) {
            //上传失败
            throw new LyException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
        return ResponseEntity.ok(url);
    }
}
