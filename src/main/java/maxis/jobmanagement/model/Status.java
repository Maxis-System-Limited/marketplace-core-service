package maxis.jobmanagement.model;

import lombok.*;

@Getter
@Setter
public class Status {
	private String id;
	private String classTypeName;
	private Long classTypeId;
	private String createdBy;
	private String createdAt;
}
