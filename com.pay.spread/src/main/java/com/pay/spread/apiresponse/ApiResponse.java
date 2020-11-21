package com.pay.spread.apiresponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse {

	public static Map<String, Object> result = new HashMap<>();

	static {
		result.put("result_code", "0000");
		result.put("result_msg", "정상처리되었습니다.");
	}

	/**
	 * HttpStatus 값을 OK(200) 상태로 응답을 내려줄 경우 사용
	 * 
	 * @param body
	 * @return
	 */
	public static ResponseEntity<Object> OK(Object body) {
		result.put("result_code", "0000");
		result.put("result_msg", "정상처리되었습니다.");
		return makeResult(body, HttpStatus.OK);
	}

	public static ResponseEntity<Object> ERROR(String error_msg, HttpStatus status) {
		result.put("result_code", "9999");
		result.put("result_msg", error_msg);
		return makeResult(null, status);
	}

	/**
	 * 사용자가 지정한 HttpStatus 값을 응답으로 내려줄 경우 사용
	 * 
	 * @param body
	 * @param status
	 * @return
	 */
	public static ResponseEntity<Object> withSTATUS(Object body, HttpStatus status) {
		result.put("result_code", "0000");
		result.put("result_msg", "정상처리되었습니다.");
		return makeResult(body, status);
	}

	/**
	 * 에러 메세지 추가가 필요할 경우 사용
	 * 
	 * @param errorMsg
	 */
	public static void addError(String errorMsg) {
		result.put("error_msg", errorMsg);
	}

	/**
	 * ApiResponse 클래스 내에서 ResponseEntity 생성 시 사용
	 * 
	 * @param body
	 * @param status
	 * @return
	 */
	private static ResponseEntity<Object> makeResult(Object body, HttpStatus status) {
		result.put("data", body);
		return new ResponseEntity<>(result, status);
	}

}
