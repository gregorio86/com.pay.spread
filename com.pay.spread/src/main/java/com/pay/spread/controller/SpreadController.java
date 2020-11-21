package com.pay.spread.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pay.spread.apiresponse.ApiResponse;
import com.pay.spread.conf.CustomAuthenticationProvider;
import com.pay.spread.conf.JwtTokenUtil;
import com.pay.spread.model.JwtRequest;
import com.pay.spread.service.SpreadService;
import com.pay.spread.util.Utils;

@RestController
@RequestMapping(produces = "application/json")
@CrossOrigin
public class SpreadController {

	@Autowired
	private CustomAuthenticationProvider authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private SpreadService spreadService;

	// @formatter:off
	@SuppressWarnings("rawtypes")
	@GetMapping(path = "/hello")
	public ResponseEntity hello(HttpServletRequest request) throws Exception {
		return ApiResponse.OK(null);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping(path = "/spreadMoney")
	public ResponseEntity spreadMoney(
			@RequestHeader("X-USER-ID") String USER_ID
			, @RequestHeader("X-ROOM-ID") long ROOM_ID
			, @RequestParam(name = "spreadTargetAmt", defaultValue = "0") long spreadTargetAmt
			, @RequestParam(name = "spreadTargetCnt", defaultValue = "0") long spreadTargetCnt, HttpServletRequest request) throws Exception {
		if (spreadTargetCnt <= 0) {
			return ApiResponse.ERROR("뿌릴 대상 인원수는 1명 이상입니다.", HttpStatus.BAD_REQUEST);
		}
		if (spreadTargetAmt <= 0) {
			return ApiResponse.ERROR("뿌릴 대상 금액은 "+spreadTargetCnt+"원 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
		}
		
		String token = Utils.generateRandomStr();
		
		spreadService.registSpreadInfo(ROOM_ID, USER_ID, token, spreadTargetAmt, spreadTargetCnt);
		
		JwtRequest authenticationRequest = new JwtRequest(USER_ID, String.valueOf(ROOM_ID));
		Authentication authentication = authenticate(authenticationRequest.getWebId(), authenticationRequest.getWebPass());
		
		final String jwt = jwtTokenUtil.generateToken(authenticationRequest.getWebId(), authenticationRequest.getWebPass());
		
		Map<String, Object> result = new HashMap<>();
		result.put("jwt", jwt);
		
		
		result.put("token", token);
		
		return ApiResponse.OK(result);
	}
	
	@GetMapping(path = "/getRandomMoney")
	public ResponseEntity getRandomMoney(
			@RequestHeader("X-USER-ID") String USER_ID
			, @RequestHeader("X-ROOM-ID") long ROOM_ID
			, @RequestParam(name = "token", defaultValue = "") String token
			) throws Exception {
		if (StringUtils.isEmpty(token)) {
			return ApiResponse.ERROR("정상적인 요청이 아닙니다.", HttpStatus.BAD_REQUEST);
		}
		
		return spreadService.updateGetAmt(ROOM_ID, token, USER_ID);
	}
	
	@GetMapping(path = "/getRandomMoneyList")
	public ResponseEntity getRandomMoneyList(
			@RequestHeader("X-USER-ID") String USER_ID
			, @RequestHeader("X-ROOM-ID") long ROOM_ID
			, @RequestParam(name = "token", defaultValue = "") String token
			) throws Exception {
		if (StringUtils.isEmpty(token)) {
			return ApiResponse.ERROR("정상적인 요청이 아닙니다.", HttpStatus.BAD_REQUEST);
		}
		
		return spreadService.getRandomMoneyList(ROOM_ID, token, USER_ID);
	}

	private Authentication authenticate(String webId, String webPass) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(webId, webPass));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
}
