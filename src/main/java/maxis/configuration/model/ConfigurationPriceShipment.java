package maxis.configuration.model;

import org.springframework.data.mongodb.core.mapping.Document;

import maxis.servicetype.model.ModelServiceType;

@Document("ModelConfigurationGeneric")
public class ConfigurationPriceShipment extends ModelConfigurationGeneric{
	public String getLogisticPartnerEntityId()
	{
		return (String) super.getProperty1();
	}

	public void setLogisticPartnerEntityId(String newLogisticPartnerEntityId)
	{
		super.setProperty1(newLogisticPartnerEntityId);
	}

	public String getShipmentEntityId()
	{
		return (String) super.getProperty2();
	}

	public void setShipmentEntityId(String newShipmentEntityId)
	{
		super.setProperty2(newShipmentEntityId);
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

	public void setConfigurationServiceArea(ConfigurationServiceAreaEnterprise newConfigurationServiceArea)
	{
		Object configurationServiceArea = newConfigurationServiceArea;
		super.setProperty4(configurationServiceArea);
		return;
	}

	public ModelServiceType getServiceType()
	{
		ModelServiceType returnServiceType = (ModelServiceType) super.getProperty5();
		return returnServiceType;
	}

	public void setServiceType(ModelServiceType newServiceType)
	{
		Object serviceType = newServiceType;
		super.setProperty5(serviceType);
		return;
	}

	public String getValue()
	{
		String returnValue = (String) super.getProperty6();
		return returnValue;
	}

	public void setThanaList(String newValue)
	{
		Object value = newValue;
		super.setProperty6(value);
		return;
	}
}
