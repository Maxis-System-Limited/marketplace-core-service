package maxis.jobmanagement.inputmodel;

import lombok.Getter;
import lombok.Setter;
import maxis.district.model.District;
import maxis.jobmanagement.model.DeliveryType;
import maxis.jobmanagement.model.PaymentType;
import maxis.jobmanagement.model.ProductType;
import maxis.thana.model.Thana;

@Getter
@Setter
public class JobInputModel {
	private String id;
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
	private String packageCategory;
	private String pickUpDetailAddress;
	private String dropOffDetailAddress;
	private String createdBy;
	private String roleOfCreatedBy;
	private String modifiedBy;
	private String boxWidth;
	private String boxHeight;
	private String boxLength;
	private String collectionAmount;
	private Long stateId;
	private Long classTypeId;
	private String roleId;
	private String tanentId;
	@SuppressWarnings("unused")
	private Long classTypeid;
	private String deliveryChannel;
	private String assignedRiderId;
	private String receivedWarehouseId;
	private String scheduledDeliveryDate;
	private String lastPerformedAction;
	private String warehouseStatus;
	
}
