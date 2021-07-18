package maxis.jobmanagement.inputmodel;

import lombok.Getter;
import lombok.Setter;
import maxis.jobmanagement.model.Job;

@Getter
@Setter
public class JobStateModificationInputModel {
	private String roleId;
	private String roleCode;//newly Added 
	private Long actionEventId;
	private Job job;
	private String createdBy;
	private String roleOfCreatedBy;
	private String tanentId;
	private String categoryType;
}
