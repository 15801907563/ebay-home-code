package com.test.ebay.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.ebay.entity.Response;

@RestController
@RequestMapping("/user")
public class UserController {

	@PostMapping(path = "/resource A")
	public Response resourceA() throws Exception {
		return Response.success("success");
	}

	@PostMapping(path = "/resource B")
	public Response resourceB() throws Exception {
		return Response.success("success");
	}

	@PostMapping(path = "/resource C")
	public Response resourceC() throws Exception {
		return Response.success("success");
	}

}