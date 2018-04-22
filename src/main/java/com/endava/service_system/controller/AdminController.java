package com.endava.service_system.controller;

import com.endava.service_system.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.endava.service_system.utils.AuthUtils;
@Controller
public class AdminController {
	
	private final AuthUtils authUtils;
	private final CategoryService categoryService;
	
	public AdminController(AuthUtils authUtils, CategoryService categoryService) {
		this.authUtils = authUtils;
		this.categoryService = categoryService;
	}
	
	@GetMapping("/admin")
	public String adminIndex() {
		return "redirect:/admin/login";
	}
	
	@GetMapping("/admin/login")
	public String adminLogin(Authentication auth) {
		if(authUtils.isLoggedIn(auth)) {
			return authUtils.isAdmin(auth)?"redirect:/admin/panel":"redirect:/";
		}
		return "adminLogin";
	}

	@GetMapping("/admin/panel")
	public String adminPanel(Model model){
		model.addAttribute("categories",categoryService.getAll());
		return "adminPanel";
	}

}
