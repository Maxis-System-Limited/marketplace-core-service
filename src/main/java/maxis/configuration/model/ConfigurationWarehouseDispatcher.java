package maxis.configuration.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("ModelConfigurationGeneric")
public class ConfigurationWarehouseDispatcher extends ModelConfigurationGeneric{
	public String getLogisticPartnerEntityId()
	{
		return (String) super.getProperty1();
	}

	public void setLogisticPartnerEntityId(String newLogisticPartnerEntityId)
	{
		super.setProperty1(newLogisticPartnerEntityId);
	}

	public String getDispatcherId()
	{
		return (String) super.getProperty2();
	}

	public void setDispatcherId(String newDispatcherId)
	{
		super.setProperty2(newDispatcherId);
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

	public ArrayList<String> getModelWarehouseList()
	{
		ArrayList<String> returnWarehouseList = (ArrayList<String>) super.getProperty4();
		return returnWarehouseList;
	}

	public void setWarehouseList(ArrayList<String> newWarehouseList)
	{
		super.setProperty4(newWarehouseList);
	}
}
