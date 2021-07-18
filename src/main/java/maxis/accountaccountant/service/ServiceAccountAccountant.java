package maxis.accountaccountant.service;

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

import maxis.accountaccountant.model.FilterAccountAccountant;
import maxis.accountaccountant.model.ModelAccountAccountant;
import maxis.accountaccountant.repository.RepositoryAccountAccountant;
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
public class ServiceAccountAccountant {

	@Autowired
	private RepositoryAccountAccountant repositoryAccountAccountant;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceAccountAccountant(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelAccountAccountant add(ModelAccountAccountant modelAccountAccountant) {

		modelAccountAccountant.setId(UUID.randomUUID().toString());
		// modelAccountAccountant.setCreatedDate(new Date().toString());
		// modelAccountAccountant.setModifiedDate(new Date().toString());

		try {
			repositoryAccountAccountant.save(modelAccountAccountant);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelAccountAccountant;
	}

	public List<ModelAccountAccountant> getList(FilterAccountAccountant filterAccountAccountant) {
		List<ModelAccountAccountant> modelAccountAccountant = new ArrayList<ModelAccountAccountant>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterAccountAccountant.getCode())) {
			modelAccountAccountant = repositoryAccountAccountant.findByCode(filterAccountAccountant.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccountAccountant.getTanentId())) {
			modelAccountAccountant = repositoryAccountAccountant.findByTanentId(filterAccountAccountant.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccountAccountant.getCreatedById())) {
			modelAccountAccountant = repositoryAccountAccountant.findByCreatedById(filterAccountAccountant.getCreatedById());
		} else {
			modelAccountAccountant = repositoryAccountAccountant.findAll();
		}
		return modelAccountAccountant;
	}

	public ModelAccountAccountant create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelAccountAccountant modelAccountAccountant = new ModelAccountAccountant();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelAccountAccountant = repositoryAccountAccountant.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelAccountAccountant == null) {
				ModelAccountAccountant newModelAccountAccountant = new ModelAccountAccountant();
				newModelAccountAccountant = (ModelAccountAccountant) UtilityProperty.initializeBaseModel(newModelAccountAccountant,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDACCOUNTACCUNTANT");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelAccountAccountant.setId(corelationId);
				newModelAccountAccountant.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelAccountAccountant.setCreatedDate(new Date().toString());
				newModelAccountAccountant.setModifiedDate(new Date().toString());
				
				newModelAccountAccountant.setRole(modelDesign.getRole());
				newModelAccountAccountant.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelAccountAccountant.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelAccountAccountant.getRole().getRoleId());
				authenticationRole.setCode(newModelAccountAccountant.getRole().getCode());
				authenticationRole.setCreatedAt(newModelAccountAccountant.getCreatedDate());
				authenticationRole.setCreatedBy(newModelAccountAccountant.getCreatedById());
				authenticationRole.setDisplayName(newModelAccountAccountant.getRole().getDisplayName());
				authenticationRole.setLevel(newModelAccountAccountant.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelAccountAccountant.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelAccountAccountant.getRole().getParentId());
				authenticationRole.setStatus(newModelAccountAccountant.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelAccountAccountant.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelAccountAccountant.getModifiedById());

				for (Step step : newModelAccountAccountant.getSteps()) {
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
				authenticationModel.setTanentId(newModelAccountAccountant.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange("http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryAccountAccountant.save(newModelAccountAccountant);
					return newModelAccountAccountant;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelAccountAccountant update(ModelAccountAccountant modelAccountAccountant) {

		ModelAccountAccountant existingModelAccountAccountant = new ModelAccountAccountant();
		String password = null;

		if (modelAccountAccountant.getAttemptId() != null) {
			existingModelAccountAccountant = repositoryAccountAccountant
					.findFirstByAttemptId(modelAccountAccountant.getAttemptId());
			if (existingModelAccountAccountant != null) {
				try {

					for (Step step : existingModelAccountAccountant.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelAccountAccountant.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryAccountAccountant.save(modelAccountAccountant);
					return modelAccountAccountant;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelAccountAccountant> getListByPropertyCodeAndValue(FilterAccountAccountant filterAccountAccountant) {
		List<ModelAccountAccountant> modelAccountAccountantList = new ArrayList<ModelAccountAccountant>();
		List<ModelAccountAccountant> modifedModelAccountAccountantList = new ArrayList<ModelAccountAccountant>();
		boolean isRecordFound = false;
		if (filterAccountAccountant.getTanentId() != null) {
			modelAccountAccountantList = repositoryAccountAccountant
					.getByTanentIdAndRoleId(filterAccountAccountant.getTanentId(), filterAccountAccountant.getRoleId());
		}

		if (modelAccountAccountantList.size() > 0) {
			for (ModelAccountAccountant modelAccountAccountant : modelAccountAccountantList) {
				for (Step step : modelAccountAccountant.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterAccountAccountant.getPropertyCode())) {
								modifedModelAccountAccountantList.add(modelAccountAccountant);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelAccountAccountantList;
	}

}
