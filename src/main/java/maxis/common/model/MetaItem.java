package maxis.common.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("MetaItem")
public class MetaItem {
	private String id;
	private String code;
	private String name;
	private String api;
	private String apiMethod;
}