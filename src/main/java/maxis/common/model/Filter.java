package maxis.common.model;

import lombok.*;

@Setter
@Getter
public class Filter {
    private String id;
	private String code;
	private String tanentId; 
	private String createdById;
	private String createdDate;
	private String propertyCode;
	private String propertyValue;
	private String roleId;
	private String roleIdOfCreatedById;
	private String modifiedById;  
	private String modifiedDate;
}
