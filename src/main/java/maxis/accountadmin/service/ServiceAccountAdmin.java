package maxis.accountadmin.service;

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

import maxis.accountadmin.model.FilterAccountAdmin;
import maxis.accountadmin.model.ModelAccountAdmin;
import maxis.accountadmin.repository.RepositoryAccountAdmin;
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

@Component
public class ServiceAccountAdmin {

	@Autowired
	private RepositoryAccountAdmin repositoryAccountAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceAccountAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelAccountAdmin add(ModelAccountAdmin modelAccountAdmin) {

		modelAccountAdmin.setId(UUID.randomUUID().toString());
		// modelAccountAdmin.setCreatedDate(new Date().toString());
		// modelAccountAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryAccountAdmin.save(modelAccountAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelAccountAdmin;
	}

	public List<ModelAccountAdmin> getList(FilterAccountAdmin filterAccountAdmin) {
		List<ModelAccountAdmin> modelAccountAdmin = new ArrayList<ModelAccountAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterAccountAdmin.getCode())) {
			modelAccountAdmin = repositoryAccountAdmin.findByCode(filterAccountAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccountAdmin.getTanentId())) {
			modelAccountAdmin = repositoryAccountAdmin.findByTanentId(filterAccountAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccountAdmin.getCreatedById())) {
			modelAccountAdmin = repositoryAccountAdmin.findByCreatedById(filterAccountAdmin.getCreatedById());
		} else {
			modelAccountAdmin = repositoryAccountAdmin.findAll();
		}
		return modelAccountAdmin;
	}

	public ModelAccountAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelAccountAdmin modelAccountAdmin = new ModelAccountAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelAccountAdmin = repositoryAccountAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelAccountAdmin == null) {
				ModelAccountAdmin newModelAccountAdmin = new ModelAccountAdmin();
				newModelAccountAdmin = (ModelAccountAdmin) UtilityProperty.initializeBaseModel(newModelAccountAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDACCOUNTADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelAccountAdmin.setId(corelationId);
				newModelAccountAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelAccountAdmin.setCreatedDate(new Date().toString());
				newModelAccountAdmin.setModifiedDate(new Date().toString());
				
				newModelAccountAdmin.setRole(modelDesign.getRole());
				newModelAccountAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelAccountAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelAccountAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelAccountAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelAccountAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelAccountAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelAccountAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelAccountAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelAccountAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelAccountAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelAccountAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelAccountAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelAccountAdmin.getModifiedById());

				for (Step step : newModelAccountAdmin.getSteps()) {
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
				authenticationModel.setTanentId(newModelAccountAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryAccountAdmin.save(newModelAccountAdmin);
					return newModelAccountAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelAccountAdmin update(ModelAccountAdmin modelAccountAdmin) {

		ModelAccountAdmin existingModelAccountAdmin = new ModelAccountAdmin();
		String password = null;

		if (modelAccountAdmin.getAttemptId() != null) {
			existingModelAccountAdmin = repositoryAccountAdmin
					.findFirstByAttemptId(modelAccountAdmin.getAttemptId());
			if (existingModelAccountAdmin != null) {
				try {

					for (Step step : existingModelAccountAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelAccountAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryAccountAdmin.save(modelAccountAdmin);
					return modelAccountAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelAccountAdmin> getListByPropertyCodeAndValue(FilterAccountAdmin filterAccountAdmin) {
		List<ModelAccountAdmin> modelAccountAdminList = new ArrayList<ModelAccountAdmin>();
		List<ModelAccountAdmin> modifedModelAccountAdminList = new ArrayList<ModelAccountAdmin>();
		boolean isRecordFound = false;
		if (filterAccountAdmin.getTanentId() != null) {
			modelAccountAdminList = repositoryAccountAdmin
					.getByTanentIdAndRoleId(filterAccountAdmin.getTanentId(), filterAccountAdmin.getRoleId());
		}

		if (modelAccountAdminList.size() > 0) {
			for (ModelAccountAdmin modelAccountAdmin : modelAccountAdminList) {
				for (Step step : modelAccountAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterAccountAdmin.getPropertyCode())) {
								modifedModelAccountAdminList.add(modelAccountAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelAccountAdminList;
	}

}
