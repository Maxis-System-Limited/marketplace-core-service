package maxis.common.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.*;

@Getter
@Setter
public class Step {
	@Id
	private String id;
	private String displayName;
	private String serialNo;
	private String type;
	private List<Context> context;
}
