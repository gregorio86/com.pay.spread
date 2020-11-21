package com.pay.spread.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pay.spread.conf.RestException;
import com.pay.spread.document.MembersDoc;
import com.pay.spread.model.JwtRequest;

@Service
public class MemberService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<MembersDoc> getMembersList(String room_id) throws Exception {
		return null;
	}

	public MembersDoc getMember(String user_id, String room_id) throws Exception {
		Query query = new Query(Criteria.where("user_id").is(user_id).and("room_id").is(Long.parseLong(room_id)));
		MembersDoc member = mongoTemplate.findOne(query, MembersDoc.class);
		return Optional.ofNullable(member).orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Not found event"));
	}

	public MembersDoc insertMember(MembersDoc body) throws Exception {
		return mongoTemplate.insert(body);
	}

	public MembersDoc getMember(JwtRequest jwt) throws Exception {
		return this.getMember(jwt.getWebId(), jwt.getWebPass());
	}

	public MembersDoc loadUserByUsername(String username) throws Exception {
		return null;
	}
}
