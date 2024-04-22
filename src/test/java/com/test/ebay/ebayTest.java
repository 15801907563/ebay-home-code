package com.test.ebay;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.SecureRandom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.test.ebay.controller.AdminController;
import com.test.ebay.entity.User;

import lombok.extern.log4j.Log4j2;

@WebMvcTest
@Log4j2
public class ebayTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AdminController adminController;

	private final static String headerName = "userInfo";

	private final User admin = new User(100000, "admin", "admin");
	private final User user = new User(123456, "user", "user");

	@Test
	public void adminAddUserSuccessTest() throws Exception {
		String adminBase64 = adminController.getBase64User(admin).getMessage();
		String user = "{\"userId\":" + generateRandomNumber()
				+ ",\"endpoint\":[\"resource A\",\"resource B\",\"resource C\"]}";
		log.info("add user info:{}", user);
		mockMvc.perform(post("/admin/addUser").header(headerName, adminBase64).contentType(MediaType.APPLICATION_JSON)
				.content(user)).andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"code\":200,\"message\":\"success\"}")));
	}

	@Test
	public void adminAddUserfailTest() throws Exception {
		String adminBase64 = adminController.getBase64User(admin).getMessage();
		String user = "{\"userId\":123456,\"endpoint\":[\"resource A\",\"resource B\",\"resource C\"]}";
		mockMvc.perform(post("/admin/addUser").header(headerName, adminBase64).contentType(MediaType.APPLICATION_JSON)
				.content(user)).andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"code\":206,\"message\":\"user already exists\"}")));
	}

	@Test
	public void userNoAccessAddUserTest() throws Exception {
		String adminBase64 = adminController.getBase64User(user).getMessage();
		String user = "{\"userId\":" + generateRandomNumber()
				+ ",\"endpoint\":[\"resource A\",\"resource B\",\"resource C\"]}";
		mockMvc.perform(post("/admin/addUser").header(headerName, adminBase64).contentType(MediaType.APPLICATION_JSON)
				.content(user)).andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"code\":200,\"message\":\"No access\"}")));
	}

	@Test
	public void userControllerSuccessTest() throws Exception {
		String adminBase64 = adminController.getBase64User(user).getMessage();
		mockMvc.perform(
				post("/user/resource A").header(headerName, adminBase64).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("{\"code\":200,\"message\":\"success\"}")));
	}

	public static int generateRandomNumber() {
		SecureRandom secureRandom = new SecureRandom();
		int randomNumber = secureRandom.nextInt(900000) + 100000;
		return randomNumber;
	}

}