package maxis.common.model;

import java.util.List;

import lombok.*;

@Getter
@Setter
public class Context {
	private String id;
	private String code;
	private String displayName;
	private String serialNo;
	private String type;
	private Boolean isHidden;
	private List<Property> properties;
}
