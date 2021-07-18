package maxis.jobmanagement.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import maxis.common.model.BaseModel;
import maxis.district.model.District;
import maxis.thana.model.Thana;
import maxis.warehouse.model.ModelWarehouse;

@Setter
@Getter
@Document("Job")
public class Job extends BaseModel {

	private Long classTypeId; // Added
	private String currentstate;
	private Long currentstateId;
	private String previousstate;
	private Long previousstateId;
	private String displayCurrentState;
	private ProductType productType;
	private DeliveryType deliveryType;
	private PaymentType paymentType;
	private District pickUpDistrict;
	private Thana pickUpThana;
	private District dropOffDistrict;
	private Thana dropOffThana;
	private String productValue;
	private String goodsDescription;
	private String packageWeight;

	private String pickUpDetailAddress;
	private String dropOffDetailAddress;
	private String createdBy;

	private String createdAt;
	private String roleOfCreatedBy;
	private String roleIdOfCreatedBy;//Must
	private String roleCode;//Newly Added
	private String modifiedBy;

	private String modifiedAt;
	private String packageCategory;
	private String width;
	private String height;
	private String length;
	private String collectionAmount;
	private String receivedWarehouseId;
	private String scheduledDeliveryDate;
	private String lastPerformedAction;
	private String warehouseStatus;
	private List<RouteWarehouse> plannedRoute;  //reference to GeneratedRoute
	private ModelWarehouse sourceWarehouse;
	private Long sourceWarehouseOrderId;
	private ModelWarehouse destinationWarehouse;
	private Long destinationWarehouseOrderId;
	////////////////////
	private String sourcing; //MAXIS OR ASSIGNED
	private String collectionType;
	private String status;
	private String logisticPartnerId;
	private String logisticPartnerCode;
	private String enterpriseCode;
	private String assignedRiderId;
	private String lastPerformedActionEvent;
	private String shipmentBundleId;
	private String assignedPNDCId;
	private String mode;
	private boolean isReturnedPickEnabled;
	private String merchantType;
	private String departureType;
	private String customerStatus; // Return,Reschedule

}
