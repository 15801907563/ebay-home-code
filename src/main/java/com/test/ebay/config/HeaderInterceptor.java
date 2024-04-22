package com.test.ebay.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.ebay.entity.Response;
import com.test.ebay.entity.User;
import com.test.ebay.entity.UserPermission;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class HeaderInterceptor implements HandlerInterceptor {

	private final static String filePath = "userInfo/users.txt";

	private final static String headerName = "userInfo";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String userInfo = request.getHeader(headerName);
		if (!StringUtils.hasText(userInfo)) {
			returnErrorMessage(response, Configuration.no_access);
			return false;
		}
		byte[] decodedBytes = Base64.getDecoder().decode(userInfo);
		String decoded = new String(decodedBytes);
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(decoded, User.class);
		log.info("The login user is:userId-{},accountName-{},role-{}",user.getUserId(),user.getAccountName(),user.getRole());
		// admin has all permissions
		if ("admin".equals(user.getRole())) {
			return true;
		}
		Map<Integer, List<String>> permissions = getPermissions(response);
		if (permissions == null) {
			return false;
		}
		List<String> permissionsList = permissions.get(user.getUserId());
		String url = request.getRequestURI().replace("/user/", "").replace("%20", " ");
		if (permissionsList != null && permissionsList.size() > 0) {
			if (permissionsList.contains(url)) {
				return true;
			} else {
				returnErrorMessage(response, Configuration.no_access);
				return false;
			}
		} else {
			returnErrorMessage(response, Configuration.user_does_not_exist);
			return false;
		}
	}

	private Map<Integer, List<String>> getPermissions(HttpServletResponse response) throws Exception {
		Map<Integer, List<String>> permissions = new HashMap<Integer, List<String>>();
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = HeaderInterceptor.class.getClassLoader().getResourceAsStream(filePath);
		if (inputStream != null) {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				reader.lines().forEach(e -> {
					try {
						if (StringUtils.hasText(e)) {
							UserPermission userPermission = mapper.readValue(e, UserPermission.class);
							permissions.put(userPermission.getUserId(), userPermission.getEndpoint());
						}
					} catch (Exception e1) {
						log.error("Permission conversion failure:", e1);
					}
				});
			} catch (Exception e) {
				log.error("Permission Read failed:", e);
				returnErrorMessage(response, Configuration.permission_Read_failed);
				return null;
			}
		} else {
			log.error("Permission Resource not found");
			returnErrorMessage(response, Configuration.no_access);
			return null;
		}
		return permissions;
	}

	private void returnErrorMessage(HttpServletResponse response, String errorMessage) throws IOException {
		Response rst = new Response();
		rst.setCode(200);
		rst.setMessage(errorMessage);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		String jsonOfRST = mapper.writeValueAsString(rst);
		out.print(jsonOfRST);
		out.flush();
	}
}
