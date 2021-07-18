package maxis.configuration.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

import maxis.warehouse.model.ModelWarehouse;

@Document("ModelConfigurationGeneric")
public class ConfigurationWarehouseRider extends ModelConfigurationGeneric{
	public String getLogisticPartnerEntityId()
	{
		return (String) super.getProperty1();
	}

	public void setLogisticPartnerEntityId(String newLogisticPartnerEntityId)
	{
		super.setProperty1(newLogisticPartnerEntityId);
	}

	public String getRiderId()
	{
		return (String) super.getProperty2();
	}

	public void setRiderId(String newRiderId)
	{
		super.setProperty2(newRiderId);
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

	public ConfigurationServiceAreaEnterprise getConfigurationServiceArea()
	{
		ConfigurationServiceAreaEnterprise returnConfigurationServiceArea = (ConfigurationServiceAreaEnterprise) super.getProperty4();
		return returnConfigurationServiceArea;
	}

	public void setConfigurationServiceArea (ConfigurationServiceAreaEnterprise newConfigurationServiceArea )
	{
		Object configurationServiceArea  = newConfigurationServiceArea ;
		super.setProperty4(configurationServiceArea );
		return;
	}

	public ArrayList<ModelWarehouse> getModelWarehouseList()
	{
		ArrayList<ModelWarehouse> returnModelWarehouseList = (ArrayList<ModelWarehouse>) super.getProperty5();
		return returnModelWarehouseList;
	}

	public void setModelWarehouseList(ArrayList<ModelWarehouse> newModelWarehouseList)
	{
		Object modelWarehouseList = newModelWarehouseList;
		super.setProperty5(modelWarehouseList);
		return;
	}
}
