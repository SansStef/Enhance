package com.sans.stef;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sans.stef.exception.OutsideUSException;
import com.sans.stef.exception.PublisherIDNotFound;
import com.sans.stef.util.IpInfoUtil;
import com.sans.stef.util.PublisherUtil;

@Controller
public class EnhanceEndpoint {

	@RequestMapping(value = "/enhance", method = RequestMethod.POST)
	@ResponseBody
    public ResponseEntity<String> doEnhance(HttpEntity<String> httpEntity) {
		
		String body = httpEntity.getBody();
		
		JSONObject json = null;
		try {
			json = EnhanceService.enhance(body);
			
		} catch (JSONException | IOException e) {
			return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
		} catch (OutsideUSException e) {
			return new ResponseEntity<String>("Error: Request is from outside the US", HttpStatus.BAD_REQUEST);
		} catch (PublisherIDNotFound e) {
			return new ResponseEntity<String>("Error: Publisher ID not found", HttpStatus.BAD_REQUEST);
		}
		
        return new ResponseEntity<String>(json.toString(), HttpStatus.ACCEPTED);
    }
	
}
