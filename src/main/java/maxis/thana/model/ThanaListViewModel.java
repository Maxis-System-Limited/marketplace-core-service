package maxis.thana.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ThanaListViewModel  {
	private List<Thana> thanas;
	private String id;
	private String status;
	private String message;
}