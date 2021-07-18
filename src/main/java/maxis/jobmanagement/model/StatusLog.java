package maxis.jobmanagement.model;

import lombok.*;

@Getter
@Setter
public class StatusLog {
	String id;
	Long classTypeId;
	Long sequenceNo;
	String instanceId;
	String currentStatus;
	String updatedBy;
	String updatedAt;
}
