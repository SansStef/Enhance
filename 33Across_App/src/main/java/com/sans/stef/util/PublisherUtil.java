package com.sans.stef.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.sans.stef.exception.PublisherIDNotFound;

public class PublisherUtil {

	private static final String GET_URL = "http://159.89.185.155:3000/api/sites/%s/demographics";
	private static final String POST_URL = "http://159.89.185.155:3000/api/publishers/find";
	
	public static String getDemographics(String siteId) throws HttpException, IOException {
		
		HttpClient client = new HttpClient();
		
		String url = String.format(GET_URL, siteId);
		
		GetMethod get = new GetMethod(url);
		
		int status = client.executeMethod(get);
		
		String response = get.getResponseBodyAsString();
		
//		System.out.println(response);
		
		get.releaseConnection();
		
		return response;
	}
	
	public static String postPublisher(String siteId) throws HttpException, IOException {
		
		HttpClient client = new HttpClient();
		
		PostMethod post = new PostMethod(POST_URL);
		
		post.addRequestHeader("accept", "application/json");
		post.addRequestHeader("Content-Type", "application/json");
		post.setRequestEntity(buildPublisherQuery(siteId));
		
		
		int status = client.executeMethod(post);
		
		String response = post.getResponseBodyAsString();
		
//		System.out.println(response);
		
		post.releaseConnection();
		
		return response;
	}
	
	public static JSONObject addPublisherDetails(JSONObject site, String siteId) throws HttpException, JSONException, IOException, PublisherIDNotFound {
		
		site.put("publisher", createPublisherDetails(siteId));
		
		return site;
	}
	
	private static JSONObject createPublisherDetails(String siteId) throws HttpException, JSONException, IOException, PublisherIDNotFound {
		JSONObject json = new JSONObject(postPublisher(siteId));

		if(!json.has("publisher") || !json.getJSONObject("publisher").has("id")) {
			throw new PublisherIDNotFound();
		}
		
		return json.getJSONObject("publisher");
	}

	public static JSONObject addDemographicDetails(JSONObject site, String siteId) throws HttpException, JSONException, IOException {
		
		site.put("demographics", createDemographicDetails(siteId));
		
		return site;
	}
	
	private static JSONObject createDemographicDetails(String siteId) throws HttpException, JSONException, IOException {
		JSONObject json = new JSONObject(getDemographics(siteId));

		double femalePercent = json.getJSONObject("demographics").getDouble("pct_female");
		double malePercent = 100 - femalePercent;
		
		JSONObject demographics = new JSONObject();
		demographics.put("female_percent", femalePercent);
		demographics.put("male_percent", malePercent);
		
		return demographics;
	}

	private static StringRequestEntity buildPublisherQuery(String siteId) throws UnsupportedEncodingException {
		String body = "{\"q\": {\"siteID\": \"" + siteId + "\"}}";
		return new StringRequestEntity(body, "application/json", "UTF-8");
	}
	
	public static void main(String[] args) throws HttpException, IOException {
		
		System.out.println(getDemographics("fool23"));
//		System.out.println(postPublisher("fool23"));
		
	}
}
