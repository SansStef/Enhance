package com.sans.stef.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONException;
import org.json.JSONObject;

import com.sans.stef.exception.OutsideUSException;

public class IpInfoUtil {

	private static final String TOKEN = "82800f53308862";
	
	private static final String URL = "http://ipinfo.io/%s/geo?token=82800f53308862";
	
	private static final String IP = "2001:4860:4860::8888";
	
	public static String getIpInfo(String ip) throws HttpException, IOException {
		
		HttpClient client = new HttpClient();
		
		String url = String.format(URL, ip);
		
		GetMethod get = new GetMethod(url);
		
		int status = client.executeMethod(get);
		
		String response = get.getResponseBodyAsString();
		
		System.out.println(response);
		
		get.releaseConnection();
		
		return response;
	}
	
	public static JSONObject addIpInfoDetails(JSONObject device, String ip) throws HttpException, JSONException, IOException, OutsideUSException {
		
		device.put("geo", createIpInfoDetails(ip));
		
		return device;
	}
	
	private static JSONObject createIpInfoDetails(String ip) throws HttpException, JSONException, IOException, OutsideUSException {
		JSONObject json = new JSONObject(getIpInfo(ip));

		JSONObject geo = new JSONObject();
		
		String country = json.getString("country");
		
		if(!"US".equals(country)) {
			throw new OutsideUSException();
		}
		
		geo.put("country", country);
		
		return geo;
	}
	
	public static void main(String[] args) throws HttpException, IOException {
		getIpInfo(IP);
	}
}
