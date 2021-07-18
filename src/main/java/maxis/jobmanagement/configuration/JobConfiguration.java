package maxis.jobmanagement.configuration;

public class JobConfiguration {

	enum Sourcing {
		DEFAULT, MAXIS
	}

	enum CollectionType {
		PICK, DROP
	}

	enum Mode {
		COLLECTION, DELIVERY, TRANSPORT
	}

	enum MerchantType {
		ENTERPRISE, PERSONAL
	}

	enum DepartureType {
		DISPATCH, SHIPMENT
	}
	
}
