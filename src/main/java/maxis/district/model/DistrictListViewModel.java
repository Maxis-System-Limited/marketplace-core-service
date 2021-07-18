package maxis.district.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DistrictListViewModel  {
	private List<District> districts;
	private String id;
	private String status;
	private String message;
}
