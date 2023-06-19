package com.smhrd.naver.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.smhrd.naver.model.TestUser;

@Mapper
public interface LoginMapper {
	
	public int naverLogin(TestUser user);

}
