package com.smhrd.haru.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor //전부 초기화 시켜주는 생성자
@NoArgsConstructor //기본 생성자
@Getter 
@Setter
public class TblNutriRecVol {
	
	private int nutri_seq;
	private int click_vol;
	private String nutri_name;

}
