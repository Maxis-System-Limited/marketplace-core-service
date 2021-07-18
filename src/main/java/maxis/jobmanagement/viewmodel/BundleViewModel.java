package maxis.jobmanagement.viewmodel;

import lombok.Getter;
import lombok.Setter;
import maxis.warehouse.model.ModelWarehouse;

@Getter
@Setter
public class BundleViewModel {
	private String id;
	private String status;
	private ModelWarehouse fromWarehouse;
	private ModelWarehouse toWarehouse;
	private String message;
	private Long previousStateId;
	private String previousStateCode;
	private Long currentStateId;
	private String currentStateCode;
}
