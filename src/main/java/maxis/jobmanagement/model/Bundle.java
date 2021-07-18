package maxis.jobmanagement.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.BaseModel;
import maxis.warehouse.model.ModelWarehouse;

@Setter
@Getter
@Document("Bundle")
public class Bundle extends BaseModel {
	
	private Long previousStateId;
	private ModelWarehouse fromWarehouse;
	private ModelWarehouse toWarehouse;
	private String previousStateCode;
	private Long currentStateId;
	private String currentStateCode;
	private String logisticPartnerId;
	private String assignedTransporterId;
	private Long numberOfJobs;
	private String routeId;
	
	
}
