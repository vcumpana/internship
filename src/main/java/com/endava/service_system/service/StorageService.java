package com.endava.service_system.service;

import com.endava.service_system.model.entities.ImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final ImageService imageService;


    @Transactional
    public String save(ImageEntity imageEntity){
        String name=imageService.save(imageEntity).getName();
        return name;
    }

    @Transactional
    public Optional<ImageEntity> getImage(String name){
        return imageService.getImageByName(name);
    }
}
