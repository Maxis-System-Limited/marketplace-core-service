package maxis.warehouse.model;



import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.OnboardModel;


@Getter
@Setter
@Document("Warehouse")
public class ModelWarehouse extends OnboardModel{
	private String warehouseOrderIdString;
	private Long warehouseOrderId;
}
