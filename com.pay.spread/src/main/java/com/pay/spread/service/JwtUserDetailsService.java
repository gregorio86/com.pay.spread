package com.pay.spread.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pay.spread.document.MembersDoc;
import com.pay.spread.model.JwtRequest;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MongoTemplate mongoTemplate;

	public MembersDoc getMemberByLoginParam(JwtRequest jwtRequest) throws Exception {
		return memberService.getMember(jwtRequest);
	}

	public MembersDoc getMemberByWebId(String webId) throws Exception {
		Query query = new Query(Criteria.where("user_id").is(webId));
		MembersDoc member = mongoTemplate.find(query, MembersDoc.class).get(0);
		return member;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

}