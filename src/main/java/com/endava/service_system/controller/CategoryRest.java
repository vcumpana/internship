package com.endava.service_system.controller;

import com.endava.service_system.model.Category;
import com.endava.service_system.service.CategoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import javax.websocket.server.PathParam;

import java.util.List;

import static com.endava.service_system.model.Roles.ROLE_ADMIN;
import static com.endava.service_system.model.Roles.ROLE_COMPANY;
import static com.endava.service_system.model.Roles.ROLE_USER;

@RestController
public class CategoryRest {

    private final CategoryService categoryService;

    public CategoryRest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
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
    @PreAuthorize("hasAnyAuthority('" + ROLE_ADMIN + "," + ROLE_COMPANY + "," + ROLE_USER + "')")
    public ResponseEntity getAllCategories() {
        return new ResponseEntity(categoryService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/category/{name}")
    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    public ResponseEntity delete(@PathVariable("name") String name) {
        try {
            List<Category> categoryList = categoryService.delete(name);
            if(!categoryList.isEmpty()) {
                return new ResponseEntity(categoryList, HttpStatus.OK);
            }else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
