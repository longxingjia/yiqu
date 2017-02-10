package com.fwrestnet;

import org.apache.http.client.methods.HttpPost;

/**
 * @ClassName: HttpPatch
 * @Description: TODO
 * 
 */

public class HttpPatch extends HttpPost {

	public HttpPatch(String url) {
		super(url);
	}

	@Override
	public String getMethod() {
		return "PATCH";
	}
}
