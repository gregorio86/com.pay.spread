package com.pay.spread.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document("takeInfosDoc")
@Getter
@Setter
public class TakeInfosDoc {

	@Id
	private String take_id;

	private String spread_id;
	private long room_id;
	private String token;
	private long target_amt;
	private String get_yn;
	private Date expire_date;
	private String target_user_id;

}
