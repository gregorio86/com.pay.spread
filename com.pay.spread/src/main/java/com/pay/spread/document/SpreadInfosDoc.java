package com.pay.spread.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document("spreadInfosDoc")
@Getter
@Setter
public class SpreadInfosDoc {

	@Id
	private String spread_id;

	private long room_id;
	private String user_id;
	private String token;
	private long spread_target_amt;
	private long spread_target_cnt;
	private Date expire_date;

}
