package maxis.district.model;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

import org.springframework.data.annotation.Id;


@Setter
@Getter
@Document("District")
public class District {
	@Id
	private String id;
	private String code;
	private String name;
	private String displayName;
	private String createdBy;
	private String createdTime;
	private String modifiedBy;
	private String modifiedTime;
	
}
