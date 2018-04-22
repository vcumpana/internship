package com.endava.service_system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Roles {
	public static final String ROLE_USER="ROLE_USER";
	public static final String ROLE_ADMIN="ROLE_ADMIN";
	public static final String ROLE_COMPANY="ROLE_COMPANY";
}
