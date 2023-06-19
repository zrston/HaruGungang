package com.smhrd.naver.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smhrd.naver.model.NaverProfile;
import com.smhrd.naver.model.OAuthToken;
import com.smhrd.naver.model.TestUser;
import com.smhrd.naver.service.LoginService;

@Controller
@CrossOrigin("http://localhost:3000")
public class LoginController {
	
	@Autowired
	LoginService service;

	@GetMapping("/")
	public String login() {
		return "login";
	}

	@GetMapping("/auth/naver/callback")
	public @ResponseBody JSONObject naverCallback(String code, String state) {

		RestTemplate rt = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "4aWJJDtBTDQlg2SlFym8");
		params.add("client_secret", "Zky5ypfo3q");
		params.add("code", code);
		params.add("state", state);

		HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<String> response = rt.exchange("https://nid.naver.com/oauth2.0/token", HttpMethod.POST,
				naverTokenRequest, String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		OAuthToken oauthToken = null;

		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("네이버 엑세스 토큰 : " + oauthToken.getAccess_token());

		RestTemplate rt2 = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> naverProfileRequest2 = new HttpEntity<>(headers2);

		// Http 요청하기 : POST 방식으로, 그리고 response 변수의 응답 받음
		ResponseEntity<String> response2 = rt2.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.POST,
				naverProfileRequest2, String.class);

		ObjectMapper objectMapper2 = new ObjectMapper();

		NaverProfile naverProfile = null;

		try {
			naverProfile = objectMapper2.readValue(response2.getBody(), NaverProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println(naverProfile.getResponse().getName());
		String name = naverProfile.getResponse().getName();
		System.out.println(naverProfile.getResponse().getEmail());
		String email = naverProfile.getResponse().getEmail();
		System.out.println(naverProfile.getResponse().getAge());
		String age = naverProfile.getResponse().getAge();
		System.out.println(naverProfile.getResponse().getGender());
		String gender = naverProfile.getResponse().getGender();
		System.out.println(naverProfile.getResponse().getId());
		String id = naverProfile.getResponse().getId();
		
		TestUser user = new TestUser(name, email, age, gender, id);
		
		JSONObject obj = service.naverLogin(user);
		
		System.out.println(obj);
		
		return obj;
	}

}
