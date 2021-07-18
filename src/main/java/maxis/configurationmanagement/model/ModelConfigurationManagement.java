package maxis.configurationmanagement.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.Action;
import maxis.common.model.MetaItem;
import maxis.common.model.TableColumn;


@Getter
@Setter
@Document("ConfigurationManagement")
public class ModelConfigurationManagement {
	@Id
	private String id;
	private String userMELRoleCode;
	private String entityMOBRoleCode;
	private Boolean addPermission;
	private String listAPI;
	private String addAPI;
	private String editAPI;
	private String deleteAPI;
	private String configurationType;
	private String configurationName;
	private String configurationShortName;
	private ArrayList<Action> actions;
	private ArrayList<TableColumn> columns;
	private ArrayList<MetaItem> meta;
	private String maxCount;
}
