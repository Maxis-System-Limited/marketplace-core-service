package maxis.warehouseadmin.service;

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
import maxis.warehouseadmin.model.FilterWarehouseAdmin;
import maxis.warehouseadmin.model.ModelWarehouseAdmin;
import maxis.warehouseadmin.repository.RepositoryWarehouseAdmin;

@Component
public class ServiceWarehouseAdmin {

	@Autowired
	private RepositoryWarehouseAdmin repositoryWarehouseAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceWarehouseAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelWarehouseAdmin add(ModelWarehouseAdmin modelWarehouseAdmin) {

		modelWarehouseAdmin.setId(UUID.randomUUID().toString());
		// modelWarehouseAdmin.setCreatedDate(new Date().toString());
		// modelWarehouseAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryWarehouseAdmin.save(modelWarehouseAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelWarehouseAdmin;
	}

	public List<ModelWarehouseAdmin> getList(FilterWarehouseAdmin filterWarehouseAdmin) {
		List<ModelWarehouseAdmin> modelWarehouseAdmin = new ArrayList<ModelWarehouseAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseAdmin.getCode())) {
			modelWarehouseAdmin = repositoryWarehouseAdmin.findByCode(filterWarehouseAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseAdmin.getTanentId())) {
			modelWarehouseAdmin = repositoryWarehouseAdmin.findByTanentId(filterWarehouseAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterWarehouseAdmin.getCreatedById())) {
			modelWarehouseAdmin = repositoryWarehouseAdmin.findByCreatedById(filterWarehouseAdmin.getCreatedById());
		} else {
			modelWarehouseAdmin = repositoryWarehouseAdmin.findAll();
		}
		return modelWarehouseAdmin;
	}

	public ModelWarehouseAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelWarehouseAdmin modelWarehouseAdmin = new ModelWarehouseAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelWarehouseAdmin = repositoryWarehouseAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelWarehouseAdmin == null) {
				ModelWarehouseAdmin newModelWarehouseAdmin = new ModelWarehouseAdmin();
				newModelWarehouseAdmin = (ModelWarehouseAdmin) UtilityProperty.initializeBaseModel(newModelWarehouseAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPWADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelWarehouseAdmin.setId(corelationId);
				newModelWarehouseAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelWarehouseAdmin.setCreatedDate(new Date().toString());
				newModelWarehouseAdmin.setModifiedDate(new Date().toString());
				
				newModelWarehouseAdmin.setRole(modelDesign.getRole());
				newModelWarehouseAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelWarehouseAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelWarehouseAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelWarehouseAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelWarehouseAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelWarehouseAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelWarehouseAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelWarehouseAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelWarehouseAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelWarehouseAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelWarehouseAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelWarehouseAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelWarehouseAdmin.getModifiedById());

				for (Step step : newModelWarehouseAdmin.getSteps()) {
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
				authenticationModel.setTanentId(newModelWarehouseAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange("http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryWarehouseAdmin.save(newModelWarehouseAdmin);
					return newModelWarehouseAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelWarehouseAdmin update(ModelWarehouseAdmin modelWarehouseAdmin) {

		ModelWarehouseAdmin existingModelWarehouseAdmin = new ModelWarehouseAdmin();
		String password = null;

		if (modelWarehouseAdmin.getAttemptId() != null) {
			existingModelWarehouseAdmin = repositoryWarehouseAdmin
					.findFirstByAttemptId(modelWarehouseAdmin.getAttemptId());
			if (existingModelWarehouseAdmin != null) {
				try {

					for (Step step : existingModelWarehouseAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelWarehouseAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryWarehouseAdmin.save(modelWarehouseAdmin);
					return modelWarehouseAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelWarehouseAdmin> getListByPropertyCodeAndValue(FilterWarehouseAdmin filterWarehouseAdmin) {
		List<ModelWarehouseAdmin> modelWarehouseAdminList = new ArrayList<ModelWarehouseAdmin>();
		List<ModelWarehouseAdmin> modifedModelWarehouseAdminList = new ArrayList<ModelWarehouseAdmin>();
		boolean isRecordFound = false;
		if (filterWarehouseAdmin.getTanentId() != null) {
			modelWarehouseAdminList = repositoryWarehouseAdmin
					.getByTanentIdAndRoleId(filterWarehouseAdmin.getTanentId(), filterWarehouseAdmin.getRoleId());
		}

		if (modelWarehouseAdminList.size() > 0) {
			for (ModelWarehouseAdmin modelWarehouseAdmin : modelWarehouseAdminList) {
				for (Step step : modelWarehouseAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterWarehouseAdmin.getPropertyCode())) {
								modifedModelWarehouseAdminList.add(modelWarehouseAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelWarehouseAdminList;
	}

}
