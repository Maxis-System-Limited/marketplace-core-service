package maxis.common.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OnboardModel extends BaseModel {
	private Role role;
	private List<Step> steps;
}
