package com.endava.service_system.service;

import java.util.Optional;

import com.endava.service_system.model.Admin;
import org.springframework.stereotype.Service;

import com.endava.service_system.dao.IAdminDao;

@Service
public class AdminService {
	
	private final IAdminDao adminDao;
	
	public AdminService(IAdminDao credentialDao) {
		this.adminDao=credentialDao;
	}
	
	public Optional<Admin> getByUsername(String username) {
		return adminDao.getByUsername(username);
	}
}
