package maxis.common;

import java.util.ArrayList;
import java.util.List;

import maxis.common.model.BaseModel;
import maxis.common.model.Context;
import maxis.common.model.Property;
import maxis.common.model.Step;

public class UtilityProperty {
	public static String getValueSteps(ArrayList<Step> steps, String propertyKey) {
		String returnValue = null;
		
		for (Step step : steps) {
			for (Context context : step.getContext()) {
				for (Property property : context.getProperties()) {
					if (property.getPropertyCode().equalsIgnoreCase(propertyKey)) {
						return property.getPropertyValue();
					}
				}
			}
		}
		return returnValue;
	}

	public static String getValueContexts(ArrayList<Context> contexts, String propertyKey) {
		String returnValue = null;

		for (Context context : contexts) {
			for (Property property : context.getProperties()) {
				if (property.getPropertyCode().equalsIgnoreCase(propertyKey)) {
					return property.getPropertyValue();
				}
			}
		}
		return returnValue;
	}

	public static String getValueProperties(List<Property> propertyList, String propertyKey) {
		String returnValue = null;

			for (Property property : propertyList) {
				if (property.getPropertyCode().equalsIgnoreCase(propertyKey)) {
					return property.getPropertyValue();
				}
		}
		return returnValue;
	}

	public static List<Step> setValues(ArrayList<Step> steps, String[] propertyKeys, String[] propertyValues) {
		for (Step step : steps) {
			for (Context context : step.getContext()) {
				for (Property property : context.getProperties()) {
					for (int i = 0; i < propertyKeys.length; i++) {
						if (property.getPropertyCode().equalsIgnoreCase(propertyKeys[i])) {
							property.setPropertyValue(propertyValues[i]);
						}
					}
				}
			}
		}
		return steps;
	}

	public static List<Step> setValues(List<Step> steps,List<Property> properties) {
		for (Step step : steps) {
			for (Context context : step.getContext()) {
				for (Property property : context.getProperties()) {
					for (Property newProperty : properties) {
						if (newProperty.getPropertyCode() != null) {
							if (property.getPropertyCode().equalsIgnoreCase(newProperty.getPropertyCode())) {
								property.setPropertyValue(newProperty.getPropertyValue());
							}
						}
						
					}
				}
			}
		}
		return steps;
	}

	public static List<Step> setValue(List<Step> steps, String propertyKey, String propertyValue) {
		for (Step step : steps) {
			for (Context context : step.getContext()) {
				for (Property property : context.getProperties()) {
					if (property.getPropertyCode().equalsIgnoreCase(propertyKey)) {
						property.setPropertyValue(propertyValue);
					}
				}
			}
		}
		return steps;
	}
	
	public static BaseModel initializeBaseModel(BaseModel baseModel, List<Property> propertyList) {
		baseModel.setCode(getValueProperties(propertyList, "USERID"));
		baseModel.setName(getValueProperties(propertyList, "NAME"));
		String createdById = getValueProperties(propertyList, "CREATOR_ORGANIZATION");
		if(createdById != null) {
			baseModel.setCreatedById(createdById);
		}else {
			baseModel.setCreatedById(getValueProperties(propertyList, "PARENT_ORGANIZATION_USER_ID"));
		}
		
		baseModel.setTanentId(getValueProperties(propertyList, "PARENT_ORGANIZATION_USER_ID"));
		//baseModel.setTanentId(getValueProperties(propertyList, "EMAIL"));
		//baseModel.setTanentId(getValueProperties(propertyList, "PHONE_NUMBER"));
//	for (Property property : propertyList) {
//
//			switch (property.getPropertyCode()) {
//			case "PARENT_ORGANIZATION_USER_ID":
//				baseModel.setTanentId(property.getPropertyValue());
//				break;
//			case "USERID":
//				baseModel.setCode(property.getPropertyValue());
//				break;
//			case "NAME":
//				baseModel.setName(property.getPropertyValue());
//				break;
//			case "CREATOR_ORGANIZATION":
//				baseModel.setCreatedById(property.getPropertyValue()); 
//				break;
//			
//			}
//
//		}
	
		return baseModel;
	}
}
