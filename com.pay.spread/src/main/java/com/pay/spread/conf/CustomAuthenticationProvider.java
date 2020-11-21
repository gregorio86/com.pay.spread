package com.pay.spread.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.pay.spread.document.MembersDoc;
import com.pay.spread.model.JwtRequest;
import com.pay.spread.service.MemberService;

@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private MemberService memberService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String user_id = (String) authentication.getPrincipal();
		String user_pw = (String) authentication.getCredentials();

		// Form에서 전달 된, name 태그 설정이 username-parameter, password-parameter로 되있는 값을 읽어온다
		if (user_id.equals("fail")) {
			return null;
		}

		// fail로 로그인 시 null 반환 > loginFail 테스트
		// ----------------------------------------------
		// DB 내 아이디, 패스워드 값 비교 로직 필요
		// ----------------------------------------------
		JwtRequest jwtRequest = new JwtRequest(user_id, user_pw);
		MembersDoc loginMember = null;
		try {
			loginMember = memberService.getMember(jwtRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("ROLE_USER"));

		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user_id, user_pw, roles);
		result.setDetails(loginMember);

		return result;

	}

	@Override

	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}