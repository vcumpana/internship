package com.endava.service_system.controller;

import com.endava.service_system.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AdminController {

	private final CategoryService categoryService;
	
	public AdminController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping("/admin")
	public String adminIndex() {
		return "redirect:/admin/panel";
	}

	@GetMapping("/admin/panel")
	public String adminPanel(Model model){
		model.addAttribute("categories",categoryService.getAll());
		return "adminPanel";
	}

}
