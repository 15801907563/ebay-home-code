package com.test.ebay.entity;

import lombok.Data;

@Data
public class Response {
	private int code;
	private String Message;

	public Response(int code, String msg) {
		super();
		this.code = code;
		this.Message = msg;
	}

	public static Response success(String msg) {
		Response res = new Response(200, msg);
		return res;
	}

	public static Response success(int code, String msg) {
		Response res = new Response(code, msg);
		return res;
	}

	public static Response failure(int code, String msg) {
		Response res = new Response(code, msg);
		return res;
	}

	public Response() {
		super();
	}
	

}
