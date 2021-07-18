package maxis.deliveryrider.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.OnboardModel;
import maxis.warehouse.model.ModelWarehouse;

@Getter
@Setter
@Document("DeliveryRider")
public class ModelDeliveryRider extends OnboardModel {
	private ModelWarehouse taggedWarehouse;
}
