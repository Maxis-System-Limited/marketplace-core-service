package maxis.jobmanagement.olmworkflow;

import lombok.*;

@Getter
@Setter
public class OLMRequestModel {
	private long classTypeId;
	private long stateId;
	private String roleId;
	private long actionEventId;
}
