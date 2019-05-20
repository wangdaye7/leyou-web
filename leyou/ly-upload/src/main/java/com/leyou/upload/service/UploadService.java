package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/17 20:17
 */

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadProperties uploadProperties;
//    public final List<String> allowTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp");
    public String upload(MultipartFile file){
        try {
            //校验文件类型
            String contentType = file.getContentType();
            System.out.println(uploadProperties.getAllowTypes());
            if (!uploadProperties.getAllowTypes().contains(contentType)) {
                throw new LyException(ExceptionEnum.FILE_TYPE_ERROR);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnum.FILE_TYPE_ERROR);
            }
//            //准备目标路径
//            File dest = new File("D:\\WJH-workSpace\\code\\java\\new\\初识项目\\leyou_upload", file.getOriginalFilename());
//            //保存文件到本地
//            file.transferTo(dest);

            //获取文件的后缀名
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            //上传文件到Fastdfs服务器
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //返回文件地址
            return uploadProperties.getBaseUrl() + storePath.getFullPath();

        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new LyException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }
    }
}
