package com.endava.service_system.controller.rest;

import com.endava.service_system.model.entities.Category;
import com.endava.service_system.service.CategoryService;
import com.endava.service_system.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CategoryRest {

    private CategoryService categoryService;
    private ServiceService serviceService;

    @PostMapping("/admin/category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity addCategory(@RequestBody Category category) {
        try {
            category.setId(-1);
            Category saveCategory = categoryService.save(category);
            return new ResponseEntity(saveCategory, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity(HttpStatus.ALREADY_REPORTED);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category")
    public ResponseEntity getAllCategories() {
        return new ResponseEntity(categoryService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/category/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteCategory(@PathVariable("name") String name) {
        try {
            List<Category> categoryList = categoryService.delete(name);
            if (!categoryList.isEmpty()) {
                return new ResponseEntity(categoryList, HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/category/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity changeCategory(@PathVariable("name") String name, @RequestBody Category category) {
        try {
            System.out.println("Name:" + name);
            System.out.println("NewName:" + category.getName());
            int entitiesUpdated = categoryService.updateName(name, category.getName());
            if (entitiesUpdated == 0) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            } else if (entitiesUpdated == 1) {
                String json="{\"count\":\"1\"}";
                return new ResponseEntity(json,HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
}
