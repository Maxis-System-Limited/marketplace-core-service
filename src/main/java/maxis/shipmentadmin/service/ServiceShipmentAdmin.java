package maxis.shipmentadmin.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import maxis.common.UtilityFunction;
import maxis.common.UtilityProperty;
import maxis.common.model.Context;
import maxis.common.model.OnboardInputModel;
import maxis.common.model.Property;
import maxis.common.model.Step;
import maxis.design.model.AuthenticationModel;
import maxis.design.model.AuthenticationRole;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;
import maxis.shipmentadmin.model.FilterShipmentAdmin;
import maxis.shipmentadmin.model.ModelShipmentAdmin;
import maxis.shipmentadmin.repository.RepositoryShipmentAdmin;

@Component
public class ServiceShipmentAdmin {

	@Autowired
	private RepositoryShipmentAdmin repositoryShipmentAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceShipmentAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelShipmentAdmin add(ModelShipmentAdmin modelShipmentAdmin) {

		modelShipmentAdmin.setId(UUID.randomUUID().toString());
		// modelShipmentAdmin.setCreatedDate(new Date().toString());
		// modelShipmentAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryShipmentAdmin.save(modelShipmentAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelShipmentAdmin;
	}

	public List<ModelShipmentAdmin> getList(FilterShipmentAdmin filterShipmentAdmin) {
		List<ModelShipmentAdmin> modelShipmentAdmin = new ArrayList<ModelShipmentAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterShipmentAdmin.getCode())) {
			modelShipmentAdmin = repositoryShipmentAdmin.findByCode(filterShipmentAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterShipmentAdmin.getTanentId())) {
			modelShipmentAdmin = repositoryShipmentAdmin.findByTanentId(filterShipmentAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterShipmentAdmin.getCreatedById())) {
			modelShipmentAdmin = repositoryShipmentAdmin.findByCreatedById(filterShipmentAdmin.getCreatedById());
		} else {
			modelShipmentAdmin = repositoryShipmentAdmin.findAll();
		}
		return modelShipmentAdmin;
	}

	public ModelShipmentAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelShipmentAdmin modelShipmentAdmin = new ModelShipmentAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelShipmentAdmin = repositoryShipmentAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelShipmentAdmin == null) {
				ModelShipmentAdmin newModelShipmentAdmin = new ModelShipmentAdmin();
				newModelShipmentAdmin = (ModelShipmentAdmin) UtilityProperty.initializeBaseModel(newModelShipmentAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPSHIPMENTADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelShipmentAdmin.setId(corelationId);
				newModelShipmentAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelShipmentAdmin.setCreatedDate(new Date().toString());
				newModelShipmentAdmin.setModifiedDate(new Date().toString());
				
				newModelShipmentAdmin.setRole(modelDesign.getRole());
				newModelShipmentAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelShipmentAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelShipmentAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelShipmentAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelShipmentAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelShipmentAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelShipmentAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelShipmentAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelShipmentAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelShipmentAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelShipmentAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelShipmentAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelShipmentAdmin.getModifiedById());

				for (Step step : newModelShipmentAdmin.getSteps()) {
					for (Context context : step.getContext()) {
						for (Property property : context.getProperties()) {

							switch (property.getPropertyCode()) {
							case "PASSWORD":
								password = property.getPropertyValue();
								break;
							case "USERID":
								userId = property.getPropertyValue();
								break;
							}

						}
					}
				}
				
				final String baseUrl = "http://maxis-elog-security.nagadpay.com/api/user/" + userId +"/exists";
				URI uri = new URI(baseUrl);
				 
				ResponseEntity<Boolean> isExistInAuthServier = restTemplate.getForEntity(uri, Boolean.class);
				
				authenticationModel.setPassword(password);
				authenticationModel.setRoles(null);
				authenticationModel.setRoles(authenticationRoles);
				authenticationModel.setStatus("");
				authenticationModel.setTanentId(newModelShipmentAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryShipmentAdmin.save(newModelShipmentAdmin);
					return newModelShipmentAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelShipmentAdmin update(ModelShipmentAdmin modelShipmentAdmin) {

		ModelShipmentAdmin existingModelShipmentAdmin = new ModelShipmentAdmin();
		String password = null;

		if (modelShipmentAdmin.getAttemptId() != null) {
			existingModelShipmentAdmin = repositoryShipmentAdmin
					.findFirstByAttemptId(modelShipmentAdmin.getAttemptId());
			if (existingModelShipmentAdmin != null) {
				try {

					for (Step step : existingModelShipmentAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelShipmentAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryShipmentAdmin.save(modelShipmentAdmin);
					return modelShipmentAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelShipmentAdmin> getListByPropertyCodeAndValue(FilterShipmentAdmin filterShipmentAdmin) {
		List<ModelShipmentAdmin> modelShipmentAdminList = new ArrayList<ModelShipmentAdmin>();
		List<ModelShipmentAdmin> modifedModelShipmentAdminList = new ArrayList<ModelShipmentAdmin>();
		boolean isRecordFound = false;
		if (filterShipmentAdmin.getTanentId() != null) {
			modelShipmentAdminList = repositoryShipmentAdmin
					.getByTanentIdAndRoleId(filterShipmentAdmin.getTanentId(), filterShipmentAdmin.getRoleId());
		}

		if (modelShipmentAdminList.size() > 0) {
			for (ModelShipmentAdmin modelShipmentAdmin : modelShipmentAdminList) {
				for (Step step : modelShipmentAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterShipmentAdmin.getPropertyCode())) {
								modifedModelShipmentAdminList.add(modelShipmentAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelShipmentAdminList;
	}

}
