package com.restful.shop.filters;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import com.restful.shop.util.HttpUtil;

public class OneTimePasswordGeneratorFilter implements ClientRequestFilter {
		protected String user;
	   protected String secret;

	   public OneTimePasswordGeneratorFilter(String user, String secret) {
	      this.user = user;
	      this.secret = secret;
	   }

	   @Override
	   public void filter(ClientRequestContext requestContext) throws IOException {
	      String otp = HttpUtil.generateOtpToken(secret);
	      requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, user + " " + otp);
	   }
}
