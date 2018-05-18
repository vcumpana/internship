package com.endava.service_system.service;

import com.endava.service_system.dao.ImageDao;
import com.endava.service_system.model.entities.ImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageDao imageDao;
    private final Random random=new Random();

    public Optional<ImageEntity> getImageByName(String name){
        return imageDao.findByNameEquals(name);
    }

    public ImageEntity save(ImageEntity imageEntity){
        String initName=imageEntity.getName();
        while (imageDao.findByNameEquals(imageEntity.getName()).isPresent()){
            imageEntity.setName(initName+random.nextInt(Integer.MAX_VALUE));
        }
        return imageDao.save(imageEntity);
    }

}
