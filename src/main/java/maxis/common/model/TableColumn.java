package maxis.common.model;

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
@Document("TableColumn")
public class TableColumn {
	@Id
	private String id;
	private String tableSource;
	private String name;
	private String selector;
	private String sortable;
	private String width;
}