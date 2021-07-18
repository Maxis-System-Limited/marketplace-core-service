package maxis.logisticpartneradmin.service;

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
import maxis.logisticpartneradmin.model.FilterLogisticPartnerAdmin;
import maxis.logisticpartneradmin.model.ModelLogisticPartnerAdmin;
import maxis.logisticpartneradmin.repository.RepositoryLogisticPartnerAdmin;

@Component
public class ServiceLogisticPartnerAdmin {

	@Autowired
	private RepositoryLogisticPartnerAdmin repositoryLogisticPartnerAdmin;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceLogisticPartnerAdmin(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelLogisticPartnerAdmin add(ModelLogisticPartnerAdmin modelLogisticPartnerAdmin) {

		modelLogisticPartnerAdmin.setId(UUID.randomUUID().toString());
		// modelLogisticPartnerAdmin.setCreatedDate(new Date().toString());
		// modelLogisticPartnerAdmin.setModifiedDate(new Date().toString());

		try {
			repositoryLogisticPartnerAdmin.save(modelLogisticPartnerAdmin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelLogisticPartnerAdmin;
	}

	public List<ModelLogisticPartnerAdmin> getList(FilterLogisticPartnerAdmin filterLogisticPartnerAdmin) {
		List<ModelLogisticPartnerAdmin> modelLogisticPartnerAdmin = new ArrayList<ModelLogisticPartnerAdmin>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterLogisticPartnerAdmin.getCode())) {
			modelLogisticPartnerAdmin = repositoryLogisticPartnerAdmin.findByCode(filterLogisticPartnerAdmin.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterLogisticPartnerAdmin.getTanentId())) {
			modelLogisticPartnerAdmin = repositoryLogisticPartnerAdmin.findByTanentId(filterLogisticPartnerAdmin.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterLogisticPartnerAdmin.getCreatedById())) {
			modelLogisticPartnerAdmin = repositoryLogisticPartnerAdmin.findByCreatedById(filterLogisticPartnerAdmin.getCreatedById());
		} else {
			modelLogisticPartnerAdmin = repositoryLogisticPartnerAdmin.findAll();
		}
		return modelLogisticPartnerAdmin;
	}

	public ModelLogisticPartnerAdmin create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelLogisticPartnerAdmin modelLogisticPartnerAdmin = new ModelLogisticPartnerAdmin();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelLogisticPartnerAdmin = repositoryLogisticPartnerAdmin.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelLogisticPartnerAdmin == null) {
				ModelLogisticPartnerAdmin newModelLogisticPartnerAdmin = new ModelLogisticPartnerAdmin();
				newModelLogisticPartnerAdmin = (ModelLogisticPartnerAdmin) UtilityProperty.initializeBaseModel(newModelLogisticPartnerAdmin,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPADMIN");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelLogisticPartnerAdmin.setId(corelationId);
				newModelLogisticPartnerAdmin.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelLogisticPartnerAdmin.setCreatedDate(new Date().toString());
				newModelLogisticPartnerAdmin.setModifiedDate(new Date().toString());
				
				newModelLogisticPartnerAdmin.setRole(modelDesign.getRole());
				newModelLogisticPartnerAdmin.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
			

				newModelLogisticPartnerAdmin.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelLogisticPartnerAdmin.getRole().getRoleId());
				authenticationRole.setCode(newModelLogisticPartnerAdmin.getRole().getCode());
				authenticationRole.setCreatedAt(newModelLogisticPartnerAdmin.getCreatedDate());
				authenticationRole.setCreatedBy(newModelLogisticPartnerAdmin.getCreatedById());
				authenticationRole.setDisplayName(newModelLogisticPartnerAdmin.getRole().getDisplayName());
				authenticationRole.setLevel(newModelLogisticPartnerAdmin.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelLogisticPartnerAdmin.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelLogisticPartnerAdmin.getRole().getParentId());
				authenticationRole.setStatus(newModelLogisticPartnerAdmin.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelLogisticPartnerAdmin.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelLogisticPartnerAdmin.getModifiedById());

				for (Step step : newModelLogisticPartnerAdmin.getSteps()) {
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
				authenticationModel.setTanentId(newModelLogisticPartnerAdmin.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryLogisticPartnerAdmin.save(newModelLogisticPartnerAdmin);
					return newModelLogisticPartnerAdmin;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelLogisticPartnerAdmin update(ModelLogisticPartnerAdmin modelLogisticPartnerAdmin) {

		ModelLogisticPartnerAdmin existingModelLogisticPartnerAdmin = new ModelLogisticPartnerAdmin();
		String password = null;

		if (modelLogisticPartnerAdmin.getAttemptId() != null) {
			existingModelLogisticPartnerAdmin = repositoryLogisticPartnerAdmin
					.findFirstByAttemptId(modelLogisticPartnerAdmin.getAttemptId());
			if (existingModelLogisticPartnerAdmin != null) {
				try {

					for (Step step : existingModelLogisticPartnerAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelLogisticPartnerAdmin.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryLogisticPartnerAdmin.save(modelLogisticPartnerAdmin);
					return modelLogisticPartnerAdmin;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelLogisticPartnerAdmin> getListByPropertyCodeAndValue(FilterLogisticPartnerAdmin filterLogisticPartnerAdmin) {
		List<ModelLogisticPartnerAdmin> modelLogisticPartnerAdminList = new ArrayList<ModelLogisticPartnerAdmin>();
		List<ModelLogisticPartnerAdmin> modifedModelLogisticPartnerAdminList = new ArrayList<ModelLogisticPartnerAdmin>();
		boolean isRecordFound = false;
		if (filterLogisticPartnerAdmin.getTanentId() != null) {
			modelLogisticPartnerAdminList = repositoryLogisticPartnerAdmin
					.getByTanentIdAndRoleId(filterLogisticPartnerAdmin.getTanentId(), filterLogisticPartnerAdmin.getRoleId());
		}

		if (modelLogisticPartnerAdminList.size() > 0) {
			for (ModelLogisticPartnerAdmin modelLogisticPartnerAdmin : modelLogisticPartnerAdminList) {
				for (Step step : modelLogisticPartnerAdmin.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterLogisticPartnerAdmin.getPropertyCode())) {
								modifedModelLogisticPartnerAdminList.add(modelLogisticPartnerAdmin);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelLogisticPartnerAdminList;
	}

}
