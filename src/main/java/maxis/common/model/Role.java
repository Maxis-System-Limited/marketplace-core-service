package maxis.common.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("Role")
public class Role {
	@Id
	private String id;
	private String roleId;
	private String parentId;
	private String code;
	private String displayName;
	private String status;
	private String level;
	private String tanentId;
	private String createdBy;
	private String createdAt;
	private String modifiedBy;
	private String modifiedAt;
}