package maxis.jobmanagement.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterBundle {
	private Long categoryid;
	private String categoryCode;
	private List<String> statusList;
}
