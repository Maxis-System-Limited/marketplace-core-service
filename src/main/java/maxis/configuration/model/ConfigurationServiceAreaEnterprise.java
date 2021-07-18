package maxis.configuration.model;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

import maxis.serviceproperty.model.ModelServiceProperty;
import maxis.thana.model.Thana;

@Document("ModelConfigurationGeneric")
public class ConfigurationServiceAreaEnterprise extends ModelConfigurationGeneric{
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

	public String getServiceAreaName()
	{
		String returnServiceAreaName = (String) super.getProperty4();
		return returnServiceAreaName;
	}

	public void setServiceAreaName(String newServiceAreaName)
	{
		Object serviceAreaName = newServiceAreaName;
		super.setProperty4(serviceAreaName);
		return;
	}

	public ArrayList<ModelServiceProperty> getServicePropertyList()
	{
		ArrayList<ModelServiceProperty> returnServicePropertyList = (ArrayList<ModelServiceProperty>) super.getProperty5();
		return returnServicePropertyList;
	}

	public void setServicePropertyList(ArrayList<ModelServiceProperty> newServicePropertyList)
	{
		Object servicePropertyList = newServicePropertyList;
		super.setProperty5(servicePropertyList);
		return;
	}

	public ArrayList<Thana> getThanaList()
	{
		ArrayList<Thana> returnThanaList = (ArrayList<Thana>) super.getProperty6();
		return returnThanaList;
	}

	public void setThanaList(ArrayList<Thana> newThanaList)
	{
		Object thanaList = newThanaList;
		super.setProperty6(thanaList);
		return;
	}
}
