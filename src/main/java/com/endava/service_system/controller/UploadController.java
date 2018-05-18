package com.endava.service_system.controller;

import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.ImageEntity;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UploadController {
    private final StorageService storageService;
    private final CompanyService companyService;
    @GetMapping("/image/{filename:.+}")
    public ResponseEntity serveFile(@PathVariable String filename) throws IOException, SQLException {
        Optional<ImageEntity> entityOptional=storageService.getImage(filename);
        if(!entityOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ImageEntity entity=entityOptional.get();
        ByteArrayResource inputStream=new ByteArrayResource(entity.getContent());
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + entity.getName() + "\"");
        headers.setContentLength(inputStream.contentLength());
        return new ResponseEntity(inputStream,headers,HttpStatus.OK);
    }

    //TODO restrict so just companies could add files
    //TODO check if file is image
    private String uploadFile(MultipartFile file) throws IOException {
        ImageEntity imageEntity=new ImageEntity();
        imageEntity.setName(file.getOriginalFilename());
        imageEntity.setContent(file.getBytes());
        String name=storageService.save(imageEntity);
        return name;
    }

    @PreAuthorize("hasRole('ROLE_COMPANY')")
    @PostMapping(value = "/company/uploadimages")
    public ResponseEntity uploadDefaultImage(Authentication authentication,@RequestParam("file") MultipartFile file) throws IOException {

        if(!isImage(file)){
            String json="{\"message\":\"File is not an image\"}";
            return new ResponseEntity(json,HttpStatus.BAD_REQUEST);
        }
        if(authentication==null){
            String json="{\"message\":\"You are not authorized\"}";
            return new ResponseEntity(json,HttpStatus.BAD_REQUEST);
        }
        Optional<Company> optionalCompany=companyService.getCompanyByUsername(authentication.getName());
        if(!optionalCompany.isPresent()){
            String json="{\"message\":\"Only companies can upload their logo\"}";
            return new ResponseEntity(json,HttpStatus.BAD_REQUEST);
        }
        Company company=companyService.getCompanyByUsername(authentication.getName()).get();
        String imageName=uploadFile(file);
        company.setImageName(imageName);
        companyService.updateWithoutCredentials(company);
        String json="{\"imageUrl\":\""+imageName+"\"}";
        return new ResponseEntity(json,HttpStatus.OK);
    }

    private boolean isImage(MultipartFile file){
        String mimetype= file.getContentType();
        String type = mimetype.split("/")[0];
        if(type.equals("image"))
            return true;
        else
            return false;
    }

}
