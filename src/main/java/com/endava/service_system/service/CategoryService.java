package com.endava.service_system.service;

import com.endava.service_system.dao.ICategoryDao;
import com.endava.service_system.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final ICategoryDao categoryDao;

    public CategoryService(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public Category save(Category category){
        return categoryDao.save(category);
    };

    public Optional<Category> getByName(String name){
        return categoryDao.getByName(name);
    };

    public List<Category> getAll(){
        return categoryDao.findAll();
    }

    public List<Category> delete(String name){
        return categoryDao.deleteByName(name);
    }

    public int updateName(String oldName,String name) {
        return categoryDao.updateCategoryByName(oldName,name);
    }
}
