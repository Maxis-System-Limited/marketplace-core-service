package maxis.serviceproperty.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("ServiceProperty")
public class ModelServiceProperty {
	@Id
	private String id;
	private String code;
	private String displayName;
	private String value;
}
