package com.pay.spread.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document("membersDoc")
@Getter
@Setter
public class MembersDoc {

	@Id
	private String id;

	private String user_id;
	private long room_id;

}
