package maxis.configuration.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("ModelConfigurationGeneric")
public class ConfigurationPNDCEnterprise extends ModelConfigurationGeneric{
	public String getLogisticPartnerEntityId()
	{
		return (String) super.getProperty1();
	}

	public void setLogisticPartnerEntityId(String newLogisticPartnerEntityId)
	{
		super.setProperty1(newLogisticPartnerEntityId);
	}

	public String getEnterpriseEntityId()
	{
		return (String) super.getProperty2();
	}

	public void setEnterpriseEntityId(String newEnterpriseEntityId)
	{
		super.setProperty2(newEnterpriseEntityId);
		return;
	}

	public String getConfigurationType()
	{
		String returnConfigurationType = (String) super.getProperty3();
		return returnConfigurationType;
	}

	public void setConfigurationType(String newConfigurationType)
	{
		Object configurationType = newConfigurationType;
		super.setProperty3(configurationType);
		return;
	}

	public ArrayList<String> getDeliveryPNDCList()
	{
		ArrayList<String> returnDeliveryPNDCList = (ArrayList<String>) super.getProperty4();
		return returnDeliveryPNDCList;
	}

	public void setServicePropertyList(ArrayList<String> newDeliveryPNDCList)
	{
		Object deliveryPNDCList = newDeliveryPNDCList;
		super.setProperty4(deliveryPNDCList);
		return;
	}
}
