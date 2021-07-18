package maxis.route.model;



import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.BaseModel;
import maxis.logisticpartner.model.ModelLogisticPartner;
import maxis.shipment.model.ModelShipment;
import maxis.shipmentcontroller.model.ModelShipmentController;
import maxis.warehouse.model.ModelWarehouse;


@Getter
@Setter
@Document("Route")
public class ModelRoute extends BaseModel{
	private String logisticPartnerId; //
	private String shipmentId; ///ParentOr
	private String type;
	private String comment;
	private Integer centralWarehouseId;
	private String status;
	private List<ModelWarehouse> warehouses;
	private List<Integer> warehouseList;
}
