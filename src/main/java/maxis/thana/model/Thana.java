package maxis.thana.model;


import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Document("Thana")
public class Thana {
	@Id
	private String id;
	private String code;
	private String name;
	private String districtId;
	private String displayName;
	private String createdBy;
	private String createdTime;
	private String modifiedBy;
	private String modifiedTime;
}
