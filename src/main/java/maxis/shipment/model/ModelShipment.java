package maxis.shipment.model;



import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.OnboardModel;


@Getter
@Setter
@Document("Shipment")
public class ModelShipment extends OnboardModel{
	
	
}
