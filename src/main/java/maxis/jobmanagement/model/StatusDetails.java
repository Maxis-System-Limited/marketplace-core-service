package maxis.jobmanagement.model;

import lombok.*;

@Getter
@Setter
public class StatusDetails {
	private String id;
	private String statusReferenceId;
	private Long classTypeId;
	private String displayName;
	private String code;
	private Long sequenceNo;
}
