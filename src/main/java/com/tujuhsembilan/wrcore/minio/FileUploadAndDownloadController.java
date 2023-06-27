package com.tujuhsembilan.wrcore.minio;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.minio.dto.MinioDTO;
import lib.i18n.utility.MessageUtil;
import lib.minio.MinioSrvc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("minio")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileUploadAndDownloadController{

    private final MinioSrvc minioSrvc;
    private final MessageUtil msg;
    @Value("${application.minio.bucketName}")
    protected String bucketName;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        minioSrvc.upload(file,bucketName);
        var o = MinioDTO.builder()
                .id(UUID.randomUUID())
                .filePath(bucketName+"_"+ file.getOriginalFilename().replace(" ", "_"))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(JsonApiModelBuilder.jsonApiModel().model(EntityModel.of(o)).meta("message",msg.get("application.success.created","Profile Picture")).build());
    }

    @GetMapping("/view")
    public void view(@RequestParam("file") String file,
            HttpServletResponse res) {
        minioSrvc.view(res, bucketName, file);
    }

}
