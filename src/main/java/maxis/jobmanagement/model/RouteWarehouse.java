package maxis.jobmanagement.model;

import lombok.Getter;
import lombok.Setter;
import maxis.warehouse.model.ModelWarehouse;

@Setter
@Getter
public class RouteWarehouse {
	private ModelWarehouse modelWarehouse;
	private Long warehouseOrderId;
	private Long orderNo;
	private boolean isExplored;
}
