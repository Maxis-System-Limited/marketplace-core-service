package maxis.dispatcheradmin.service;

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
import maxis.dispatcheradmin.model.FilterDispatcherAdmin;
import maxis.dispatcheradmin.model.ModelDispatcherAdmin;
import maxis.dispatcheradmin.repository.RepositoryDispatcherAdmin;

@Component
public class ServiceDispatcherAdmin {

	@Autowired
	private RepositoryDispatcherAdmin repositoryDispatcherAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceDispatcherAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelDispatcherAdmin add(ModelDispatcherAdmin modelDispatcherAdmin) {

		modelDispatcherAdmin.setId(UUID.randomUUID().toString());
		// modelDispatcherAdmin.setCreatedDate(new Date().toString());
		// modelDispatcherAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryDispatcherAdmin.save(modelDispatcherAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelDispatcherAdmin;
	}

	public List<ModelDispatcherAdmin> getList(FilterDispatcherAdmin filterDispatcherAdmin) {
		List<ModelDispatcherAdmin> modelDispatcherAdmin = new ArrayList<ModelDispatcherAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterDispatcherAdmin.getCode())) {
			modelDispatcherAdmin = repositoryDispatcherAdmin.findByCode(filterDispatcherAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDispatcherAdmin.getTanentId())) {
			modelDispatcherAdmin = repositoryDispatcherAdmin.findByTanentId(filterDispatcherAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterDispatcherAdmin.getCreatedById())) {
			modelDispatcherAdmin = repositoryDispatcherAdmin.findByCreatedById(filterDispatcherAdmin.getCreatedById());
		} else {
			modelDispatcherAdmin = repositoryDispatcherAdmin.findAll();
		}
		return modelDispatcherAdmin;
	}

	public ModelDispatcherAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelDispatcherAdmin modelDispatcherAdmin = new ModelDispatcherAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelDispatcherAdmin = repositoryDispatcherAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelDispatcherAdmin == null) {
				ModelDispatcherAdmin newModelDispatcherAdmin = new ModelDispatcherAdmin();
				newModelDispatcherAdmin = (ModelDispatcherAdmin) UtilityProperty.initializeBaseModel(newModelDispatcherAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelDispatcherAdmin.setId(corelationId);
				newModelDispatcherAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelDispatcherAdmin.setCreatedDate(new Date().toString());
				newModelDispatcherAdmin.setModifiedDate(new Date().toString());
				
				newModelDispatcherAdmin.setRole(modelDesign.getRole());
				newModelDispatcherAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelDispatcherAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelDispatcherAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelDispatcherAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelDispatcherAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelDispatcherAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelDispatcherAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelDispatcherAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelDispatcherAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelDispatcherAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelDispatcherAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelDispatcherAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelDispatcherAdmin.getModifiedById());

				for (Step step : newModelDispatcherAdmin.getSteps()) {
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
				authenticationModel.setTanentId(newModelDispatcherAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryDispatcherAdmin.save(newModelDispatcherAdmin);
					return newModelDispatcherAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelDispatcherAdmin update(ModelDispatcherAdmin modelDispatcherAdmin) {

		ModelDispatcherAdmin existingModelDispatcherAdmin = new ModelDispatcherAdmin();
		String password = null;

		if (modelDispatcherAdmin.getAttemptId() != null) {
			existingModelDispatcherAdmin = repositoryDispatcherAdmin
					.findFirstByAttemptId(modelDispatcherAdmin.getAttemptId());
			if (existingModelDispatcherAdmin != null) {
				try {

					for (Step step : existingModelDispatcherAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelDispatcherAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryDispatcherAdmin.save(modelDispatcherAdmin);
					return modelDispatcherAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelDispatcherAdmin> getListByPropertyCodeAndValue(FilterDispatcherAdmin filterDispatcherAdmin) {
		List<ModelDispatcherAdmin> modelDispatcherAdminList = new ArrayList<ModelDispatcherAdmin>();
		List<ModelDispatcherAdmin> modifedModelDispatcherAdminList = new ArrayList<ModelDispatcherAdmin>();
		boolean isRecordFound = false;
		if (filterDispatcherAdmin.getTanentId() != null) {
			modelDispatcherAdminList = repositoryDispatcherAdmin
					.getByTanentIdAndRoleId(filterDispatcherAdmin.getTanentId(), filterDispatcherAdmin.getRoleId());
		}

		if (modelDispatcherAdminList.size() > 0) {
			for (ModelDispatcherAdmin modelDispatcherAdmin : modelDispatcherAdminList) {
				for (Step step : modelDispatcherAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterDispatcherAdmin.getPropertyCode())) {
								modifedModelDispatcherAdminList.add(modelDispatcherAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelDispatcherAdminList;
	}

}
