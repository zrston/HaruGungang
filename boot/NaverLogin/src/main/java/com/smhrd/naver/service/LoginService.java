package com.smhrd.naver.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smhrd.naver.mapper.LoginMapper;
import com.smhrd.naver.model.TestUser;

@Service
public class LoginService {

	@Autowired
	LoginMapper mapper;

	public JSONObject naverLogin(TestUser user) {

		int cnt = mapper.naverLogin(user);

		JSONObject obj = new JSONObject();

		if (cnt > 0) {
			obj.put("user", user);
		} else {
			System.out.println("네이버 로그인 실패");
		}

		return obj;

	}

}
