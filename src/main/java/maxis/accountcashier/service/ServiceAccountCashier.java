package maxis.accountcashier.service;

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

import maxis.accountcashier.model.FilterAccountCashier;
import maxis.accountcashier.model.ModelAccountCashier;
import maxis.accountcashier.repository.RepositoryAccountCashier;
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
public class ServiceAccountCashier {

	@Autowired
	private RepositoryAccountCashier repositoryAccountCashier;
	@Autowired
	private ServiceDesign serviceDesign;

	private RestTemplate restTemplate;

	public ServiceAccountCashier(RestTemplateBuilder restTemplateBuilder) {
		super();
		this.restTemplate = restTemplateBuilder.build();
	}

	public ModelAccountCashier add(ModelAccountCashier modelAccountCashier) {

		modelAccountCashier.setId(UUID.randomUUID().toString());
		// modelAccountCashier.setCreatedDate(new Date().toString());
		// modelAccountCashier.setModifiedDate(new Date().toString());

		try {
			repositoryAccountCashier.save(modelAccountCashier);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelAccountCashier;
	}

	public List<ModelAccountCashier> getList(FilterAccountCashier filterAccountCashier) {
		List<ModelAccountCashier> modelAccountCashier = new ArrayList<ModelAccountCashier>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterAccountCashier.getCode())) {
			modelAccountCashier = repositoryAccountCashier.findByCode(filterAccountCashier.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccountCashier.getTanentId())) {
			modelAccountCashier = repositoryAccountCashier.findByTanentId(filterAccountCashier.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccountCashier.getCreatedById())) {
			modelAccountCashier = repositoryAccountCashier.findByCreatedById(filterAccountCashier.getCreatedById());
		} else {
			modelAccountCashier = repositoryAccountCashier.findAll();
		}
		return modelAccountCashier;
	}

	public ModelAccountCashier create(OnboardInputModel onboardInputModel) throws URISyntaxException {

		ModelAccountCashier modelAccountCashier = new ModelAccountCashier();
		String corelationId = UUID.randomUUID().toString();
		String password = null;
		String userId = null;
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
	


		if (onboardInputModel.getAttemptId() != null) {
			modelAccountCashier = repositoryAccountCashier.findByAttemptId(onboardInputModel.getAttemptId());

			if (modelAccountCashier == null) {
				ModelAccountCashier newModelAccountCashier = new ModelAccountCashier();
				newModelAccountCashier = (ModelAccountCashier) UtilityProperty.initializeBaseModel(newModelAccountCashier,
						onboardInputModel.getExtendedPropertyList().getPropertyList());
				
				FilterDesign filterDesign = new FilterDesign();
				filterDesign.setCode("MAXISELLPDACCOUNTCASHIER");
				ModelDesign modelDesign = new ModelDesign();
				modelDesign = serviceDesign.getByCode(filterDesign);

				List<Step> steps = new ArrayList<Step>();
				steps = UtilityProperty.setValues(modelDesign.getSteps(),
						onboardInputModel.getExtendedPropertyList().getPropertyList());

				newModelAccountCashier.setId(corelationId);
				newModelAccountCashier.setAttemptId(onboardInputModel.getAttemptId());
				
				newModelAccountCashier.setCreatedDate(new Date().toString());
				newModelAccountCashier.setModifiedDate(new Date().toString());
				
				newModelAccountCashier.setRole(modelDesign.getRole());
				newModelAccountCashier.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
				

				newModelAccountCashier.setSteps(steps);

				AuthenticationRole authenticationRole = new AuthenticationRole();
				authenticationRole.setRoleId(newModelAccountCashier.getRole().getRoleId());
				authenticationRole.setCode(newModelAccountCashier.getRole().getCode());
				authenticationRole.setCreatedAt(newModelAccountCashier.getCreatedDate());
				authenticationRole.setCreatedBy(newModelAccountCashier.getCreatedById());
				authenticationRole.setDisplayName(newModelAccountCashier.getRole().getDisplayName());
				authenticationRole.setLevel(newModelAccountCashier.getRole().getLevel());
				authenticationRole.setModifiedBy(newModelAccountCashier.getRole().getModifiedBy());
				authenticationRole.setParentId(newModelAccountCashier.getRole().getParentId());
				authenticationRole.setStatus(newModelAccountCashier.getRole().getStatus());

				List<AuthenticationRole> authenticationRoles = new ArrayList<AuthenticationRole>();
				authenticationRoles.add(authenticationRole);

				AuthenticationModel authenticationModel = new AuthenticationModel();
				authenticationModel.setCorrelationId(corelationId);
				authenticationModel.setCreatedBy(newModelAccountCashier.getCreatedById());
				authenticationModel.setDescription("");
				authenticationModel.setModifiedBy(newModelAccountCashier.getModifiedById());

				for (Step step : newModelAccountCashier.getSteps()) {
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
				authenticationModel.setTanentId(newModelAccountCashier.getTanentId());
				authenticationModel.setUserCode(userId);
				authenticationModel.setUserName(userId);

				HttpEntity<AuthenticationModel> entity = new HttpEntity<>(authenticationModel, httpHeaders);
				try {
					if(!isExistInAuthServier.getBody()) {
						restTemplate.exchange(
								"http://maxis-elog-security.nagadpay.com/api/user", HttpMethod.POST, entity, String.class);
					}
					repositoryAccountCashier.save(newModelAccountCashier);
					return newModelAccountCashier;
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}

		return null;
	}

	public ModelAccountCashier update(ModelAccountCashier modelAccountCashier) {

		ModelAccountCashier existingModelAccountCashier = new ModelAccountCashier();
		String password = null;

		if (modelAccountCashier.getAttemptId() != null) {
			existingModelAccountCashier = repositoryAccountCashier
					.findFirstByAttemptId(modelAccountCashier.getAttemptId());
			if (existingModelAccountCashier != null) {
				try {

					for (Step step : existingModelAccountCashier.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									password = property.getPropertyValue();
									break;
								}
							}
						}
					}
					for (Step step : modelAccountCashier.getSteps()) {
						for (Context context : step.getContext()) {
							for (Property property : context.getProperties()) {
								if (property.getPropertyCode().equals("PASSWORD")) {
									property.setPropertyValue(password);
									break;
								}
							}
						}
					}
					repositoryAccountCashier.save(modelAccountCashier);
					return modelAccountCashier;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

		return null;
	}

	public List<ModelAccountCashier> getListByPropertyCodeAndValue(FilterAccountCashier filterAccountCashier) {
		List<ModelAccountCashier> modelAccountCashierList = new ArrayList<ModelAccountCashier>();
		List<ModelAccountCashier> modifedModelAccountCashierList = new ArrayList<ModelAccountCashier>();
		boolean isRecordFound = false;
		if (filterAccountCashier.getTanentId() != null) {
			modelAccountCashierList = repositoryAccountCashier
					.getByTanentIdAndRoleId(filterAccountCashier.getTanentId(), filterAccountCashier.getRoleId());
		}

		if (modelAccountCashierList.size() > 0) {
			for (ModelAccountCashier modelAccountCashier : modelAccountCashierList) {
				for (Step step : modelAccountCashier.getSteps()) {
					isRecordFound = false;
					for (Context context : step.getContext()) {
						if (isRecordFound) {
							break;
						}
						for (Property property : context.getProperties()) {
							if (property.getPropertyCode().equals(filterAccountCashier.getPropertyCode())) {
								modifedModelAccountCashierList.add(modelAccountCashier);
								isRecordFound = true;
								break;
							}
						}
					}
				}
			}
		}

		return modelAccountCashierList;
	}

}
