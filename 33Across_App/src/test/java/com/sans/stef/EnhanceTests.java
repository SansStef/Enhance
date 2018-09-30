package com.sans.stef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.sans.stef.exception.OutsideUSException;
import com.sans.stef.exception.PublisherIDNotFound;

public class EnhanceTests {
	
	private String request = "{" + 
			"\"site\": {" + 
			"\"id\": \"foo123\"," + 
			"\"page\": \"http://www.foo.com/why-foo\"" + 
			"}," + 
			"\"device\": {" + 
			"\"ip\": \"69.250.196.118\"\n" + 
			"}," + 
			"\"user\": {" + 
			"\"id\": \"9cb89r\"" + 
			"}" + 
			"}"; 
	
	
	@Test
	public void testEnhance() throws HttpException, JSONException, IOException, OutsideUSException, PublisherIDNotFound {
		JSONObject json = EnhanceService.enhance(request);
		
		assertTrue(json.getJSONObject("site").has("publisher"));
		assertTrue(json.getJSONObject("site").getJSONObject("publisher").has("id"));
		assertTrue(json.getJSONObject("site").getJSONObject("publisher").has("name"));

		assertTrue(json.getJSONObject("site").has("demographics"));
		assertTrue(json.getJSONObject("site").getJSONObject("demographics").has("female_percent"));
		assertTrue(json.getJSONObject("site").getJSONObject("demographics").has("male_percent"));

		assertTrue(json.getJSONObject("device").has("geo"));
		assertTrue(json.getJSONObject("device").getJSONObject("geo").has("country"));
		assertEquals("US", json.getJSONObject("device").getJSONObject("geo").getString("country"));
	}

}
