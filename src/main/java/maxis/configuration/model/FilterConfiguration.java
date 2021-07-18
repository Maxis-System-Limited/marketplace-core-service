package maxis.configuration.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilterConfiguration {
    private String id;
	private String serviceType;
	private String logisticPartnerId;
	private String enterpriseId;
	private String dispatcherId;
	private String riderId;
	private String warehouseId;
	private String shipmentId;
	private String transporterId;
	private String property1;
	private String property2;
	private String property3;
	
	public static String SERVICE_TYPE_SERVICE_AREA_ENTERPRISE = "SERVICE_TYPE_SERVICE_AREA_ENTERPRISE";
	public static String SERVICE_TYPE_PNDC_ENTERPRISE = "SERVICE_TYPE_PNDC_ENTERPRISE";
	public static String SERVICE_TYPE_PRICE_ENTERPRISE = "SERVICE_TYPE_PRICE_ENTERPRISE";

	public static String SERVICE_TYPE_SERVICE_AREA_WAREHOUSE = "SERVICE_TYPE_SERVICE_AREA_WAREHOUSE";
	public static String SERVICE_TYPE_SERVICE_AREA_PNDC = "SERVICE_TYPE_SERVICE_AREA_PNDC";
	public static String SERVICE_TYPE_SERVICE_AREA_RIDER = "SERVICE_TYPE_SERVICE_AREA_RIDER";

	public static String SERVICE_TYPE_PRICE_SHIPMENT = "SERVICE_TYPE_PRICE_SHIPMENT";
	public static String SERVICE_TYPE_PRICE_TRANSPORTER = "SERVICE_TYPE_PRICE_TRANSPORTER";

	public static String SERVICE_TYPE_WAREHOUSE_DISPATCHER = "SERVICE_TYPE_WAREHOUSE_DISPATCHER";

	public static String SERVICE_TYPE_SERVICE_AREA_LOGISTIC_PARTNER = "SERVICE_TYPE_SERVICE_AREA_LOGISTIC_PARTNER";
}
