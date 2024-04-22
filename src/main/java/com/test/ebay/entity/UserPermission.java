package com.test.ebay.entity;

import java.util.List;

import lombok.Data;

@Data
public class UserPermission {
	private int userId;
	private List<String> endpoint;

	public UserPermission(int userId, List<String> endpoint) {
		super();
		this.userId = userId;
		this.endpoint = endpoint;
	}

	public UserPermission() {
		super();
	}

}
