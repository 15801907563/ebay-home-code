package com.test.ebay.entity;

import lombok.Data;

@Data
public class User {
	private int userId;
	private String accountName;
	private String role;

	public User() {
		super();
	}

	public User(int userId, String accountName, String role) {
		super();
		this.userId = userId;
		this.accountName = accountName;
		this.role = role;
	}

}
