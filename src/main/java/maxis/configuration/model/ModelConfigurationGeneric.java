package maxis.configuration.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("ModelConfigurationGeneric")
public class ModelConfigurationGeneric {
	@Id
	private String id;
	
	private Object property1;
	private Object property2;
	private Object property3;
	private Object property4;
	private Object property5;
	private Object property6;
	private Object property7;
	private Object property8;
}
