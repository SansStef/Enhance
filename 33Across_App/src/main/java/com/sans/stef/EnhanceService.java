package com.sans.stef;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.sans.stef.exception.OutsideUSException;
import com.sans.stef.exception.PublisherIDNotFound;
import com.sans.stef.util.IpInfoUtil;
import com.sans.stef.util.PublisherUtil;

public class EnhanceService {

	public static JSONObject enhance(String body) throws JSONException, HttpException, IOException, OutsideUSException, PublisherIDNotFound {
		JSONObject json = new JSONObject(body);
		
		JSONObject site = json.getJSONObject("site");
		JSONObject device = json.getJSONObject("device");
		
		String siteId = site.getString("id");
		String ip = device.getString("ip");
		
		IpInfoUtil.addIpInfoDetails(device, ip);

		PublisherUtil.addPublisherDetails(site, siteId);
		PublisherUtil.addDemographicDetails(site, siteId);
		
		return json;
	}
}
