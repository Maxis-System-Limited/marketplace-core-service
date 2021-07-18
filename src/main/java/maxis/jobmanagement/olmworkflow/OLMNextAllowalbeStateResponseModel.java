package maxis.jobmanagement.olmworkflow;

import lombok.*;

@Getter
@Setter
public class OLMNextAllowalbeStateResponseModel {
	private String type;
	private StateAttribute attributes;
	private String id;
}
