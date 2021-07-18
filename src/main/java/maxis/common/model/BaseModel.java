package maxis.common.model;



import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseModel {
	@Id
	private String id;
	private String code;  // user id
	private String name;   // user name 
	private String tanentId;  // Organisation Entity id
	private String attemptId;
	private String createdById; // Creating Entity id
	private String createdDate;
	private String roleIdOfCreatedById;
	private String modifiedById;  
	private String modifiedDate;
	
	
	
}
