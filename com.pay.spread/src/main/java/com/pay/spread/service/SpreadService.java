package com.pay.spread.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pay.spread.apiresponse.ApiResponse;
import com.pay.spread.document.MembersDoc;
import com.pay.spread.document.SpreadInfosDoc;
import com.pay.spread.document.TakeInfosDoc;
import com.pay.spread.util.Utils;

@Service
public class SpreadService {

	@Autowired
	private MongoTemplate mongoTemplate;

	// 멤버 등록 처리; ROOM_ID, USER_ID
	public MembersDoc registMember(long room_id, String user_id) throws Exception {
		MembersDoc body = new MembersDoc();
		body.setRoom_id(room_id);
		body.setUser_id(user_id);
		return mongoTemplate.insert(body);
	}

	// 멤버 조회; ROOM_ID, USER_ID
	public MembersDoc getMember(String room_id, String user_id) throws Exception {
		Query query = new Query(Criteria.where("user_id").is(user_id).and("room_id").is(room_id));
		return mongoTemplate.findOne(query, MembersDoc.class);
	}

	// 뿌리기 정보 등록; SPREAD_ID, ROOM_ID, USER_ID, TOKEN, SPREAD_TARGET_AMT,
	// SPREAD_TARGET_CNT, EXPIRE_DATE
	public void registSpreadInfo(long room_id, String user_id, String token, long spread_target_amt, long spread_target_cnt) throws Exception {
		Query query = new Query(Criteria.where("user_id").is(user_id).and("room_id").is(room_id));
		MembersDoc members = mongoTemplate.findOne(query, MembersDoc.class);
		if (members == null) {
			MembersDoc body = new MembersDoc();
			body.setRoom_id(room_id);
			body.setUser_id(user_id);
			mongoTemplate.insert(body);
		}

		SpreadInfosDoc spreadInfo = new SpreadInfosDoc();
		spreadInfo.setRoom_id(room_id);
		spreadInfo.setUser_id(user_id);
		spreadInfo.setToken(token);
		spreadInfo.setSpread_target_amt(spread_target_amt);
		spreadInfo.setSpread_target_cnt(spread_target_cnt);
		spreadInfo.setExpire_date(Utils.addDay(7));
		spreadInfo = mongoTemplate.insert(spreadInfo);

		long targetAmt = 0;
		for (long i = 0; i < spread_target_cnt; i++) {
			if (i == spread_target_cnt - 1) {
				targetAmt = spread_target_amt;
			} else {
				targetAmt = Utils.randomDivision(spread_target_amt);
				spread_target_amt -= targetAmt;
			}
			TakeInfosDoc takeInfo = new TakeInfosDoc();
			takeInfo.setSpread_id(spreadInfo.getSpread_id());
			takeInfo.setRoom_id(room_id);
			takeInfo.setToken(token);
			takeInfo.setTarget_amt(targetAmt);
			takeInfo.setGet_yn("N");
			takeInfo.setExpire_date(Utils.addMinutes(10));
			mongoTemplate.insert(takeInfo);
		}
	}

	// 뿌린 금액에 대한 할당 정보 등록; SPREAD_ID, ROOM_ID, TOKEN, TARGET_AMT, GET_YN,
	// EXPIRE_DATE, TARGET_USER_ID
	public void registSepreatedAmt() throws Exception {

	}

	// 금액 가져가기; SPREAD_ID, ROOM_ID, TOKEN, TARGET_AMT, GET_YN,
	// EXPIRE_DATE, TARGET_USER_ID
	public ResponseEntity updateGetAmt(long room_id, String token, String target_user_id) throws Exception {
		Query userQuery = new Query(Criteria.where("user_id").is(target_user_id).and("room_id").is(room_id));
		MembersDoc members = mongoTemplate.findOne(userQuery, MembersDoc.class);
		if (members == null) {
			MembersDoc body = new MembersDoc();
			body.setRoom_id(room_id);
			body.setUser_id(target_user_id);
			mongoTemplate.insert(body);
		}

		Query querySelf = new Query(Criteria.where("room_id").is(room_id).and("user_id").is(target_user_id).and("token").is(token));
		SpreadInfosDoc spreadInfo = mongoTemplate.findOne(querySelf, SpreadInfosDoc.class);
		if (spreadInfo != null) {
			return ApiResponse.ERROR("본인은 가져갈 수 없는 돈입니다.", HttpStatus.IM_USED);
		}
//		private String take_id;
//		private String spread_id;
//		private long room_id;
//		private String token;
//		private long target_amt;
//		private String get_yn;
//		private Date expire_date;
//		private String target_user_id;
		Query queryDup = new Query(Criteria.where("room_id").is(room_id).and("token").is(token).and("get_yn").is("Y").and("target_user_id").is(target_user_id));
		TakeInfosDoc takeInfoDup = mongoTemplate.findOne(queryDup, TakeInfosDoc.class);
		if (takeInfoDup != null) {
			return ApiResponse.ERROR("이미 받은 사용자 입니다.", HttpStatus.ALREADY_REPORTED);
		}

		long getAmt = 0;
		Query query = new Query(Criteria.where("room_id").is(room_id).and("token").is(token));
		List<TakeInfosDoc> takeInfoList = mongoTemplate.find(query, TakeInfosDoc.class);
		if (takeInfoList == null || takeInfoList.size() == 0) {
			return ApiResponse.ERROR("같은 방의 사용자가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}

		Query canBeTakeQuery = new Query(Criteria.where("room_id").is(room_id).and("token").is(token).and("get_yn").is("N"));
		List<TakeInfosDoc> canBeTakeInfoList = mongoTemplate.find(canBeTakeQuery, TakeInfosDoc.class);
		if (canBeTakeInfoList == null || canBeTakeInfoList.size() == 0) {
			return ApiResponse.ERROR("받을 수 있는 금액이 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		TakeInfosDoc takeInfo = canBeTakeInfoList.get(0);
		if (Utils.isOverTodate(takeInfo.getExpire_date())) {
			return ApiResponse.ERROR("받을 수 있는 유효 시간이 지났습니다.", HttpStatus.REQUEST_TIMEOUT);
		} else {
			getAmt = takeInfo.getTarget_amt();
			Update update = new Update();
			update.set("get_yn", "Y");
			update.set("target_user_id", target_user_id);
			Query updateQuery = new Query(Criteria.where("room_id").is(room_id).and("token").is(token).and("get_yn").is("N"));
			mongoTemplate.updateFirst(updateQuery, update, TakeInfosDoc.class);
			return ApiResponse.OK(getAmt);
		}
	}

	public ResponseEntity getRandomMoneyList(long room_id, String token, String user_id) throws Exception {
		Query querySelf = new Query(Criteria.where("room_id").is(room_id).and("user_id").is(user_id).and("token").is(token));
		SpreadInfosDoc spreadInfo = mongoTemplate.findOne(querySelf, SpreadInfosDoc.class);

		if (spreadInfo == null) {
			return ApiResponse.ERROR("뿌리기를 한적이 없는 사용자입니다.", HttpStatus.NOT_FOUND);
		} else {
			if (Utils.isOverTodate(spreadInfo.getExpire_date())) {
				return ApiResponse.ERROR("조회 가능한 유효기간이 경과하였습니다.", HttpStatus.REQUEST_TIMEOUT);
			} else {
				Query query = new Query(Criteria.where("room_id").is(room_id).and("token").is(token));
				List<TakeInfosDoc> takeInfoList = mongoTemplate.find(query, TakeInfosDoc.class);
				long remainAmt = spreadInfo.getSpread_target_amt();
				for (TakeInfosDoc takeInfo : takeInfoList) {
					if (StringUtils.equals(takeInfo.getGet_yn(), "Y")) {
						remainAmt -= takeInfo.getTarget_amt();
					}
				}
				Map<String, Object> result = new HashMap<>();
				result.put("SPREAD_INFO", spreadInfo);
				result.put("REMAIN_AMT", remainAmt);
				result.put("TAKE_INFO", takeInfoList);
				return ApiResponse.OK(result);
			}
		}
	}

}
