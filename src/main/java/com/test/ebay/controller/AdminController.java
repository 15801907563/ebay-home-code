package com.test.ebay.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ebay.entity.Response;
import com.test.ebay.entity.User;
import com.test.ebay.entity.UserPermission;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/admin")
public class AdminController {

	private final static String filePath = "src/main/resources/userInfo/users.txt";

	@PostMapping(path = "/addUser")
	public Response addUser(@RequestBody UserPermission userPermission) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String user = mapper.writeValueAsString(userPermission);
		Path path = Paths.get(filePath);
		try {
			List<String> lines = Files.readAllLines(path);
			Map<Integer, UserPermission> userMap = new HashMap<Integer, UserPermission>();
			for (String string : lines) {
				if (StringUtils.hasText(string)) {
					UserPermission userPer = mapper.readValue(string, UserPermission.class);
					userMap.put(userPer.getUserId(), userPer);
				}
			}
			if (userMap.containsKey(userPermission.getUserId())) {
				return Response.failure(206, "user already exists");
			}
			lines.add(user);
			Files.write(path, lines, StandardOpenOption.WRITE);
		} catch (Exception e) {
			log.error("write file error:", e);
			return Response.failure(205, "write file error");
		}
		return Response.success("success");
	}

	@PostMapping(path = "/getBase64User")
	public Response getBase64User(@RequestBody User user) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String u = mapper.writeValueAsString(user);
		String encoded = Base64.getEncoder().encodeToString(u.getBytes());
		return Response.success(encoded);
	}

}