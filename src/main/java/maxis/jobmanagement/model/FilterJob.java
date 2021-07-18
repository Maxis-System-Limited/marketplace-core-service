package maxis.jobmanagement.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.Filter;

@Getter
@Setter
public class FilterJob extends Filter{
	private String roleCode;
	private Long categoryid;
	private String categoryCode;
	private List<String> statusList;
}
