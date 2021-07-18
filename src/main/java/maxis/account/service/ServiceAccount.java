package maxis.account.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.account.model.FilterAccount;
import maxis.account.model.ModelAccount;
import maxis.account.repository.RepositoryAccount;
import maxis.common.UtilityFunction;
import maxis.common.UtilityProperty;
import maxis.common.model.OnboardInputModel;
import maxis.common.model.Step;
import maxis.design.model.FilterDesign;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;

@Service
public class ServiceAccount {

	@Autowired
	private RepositoryAccount repositoryAccount;
	@Autowired
	private ServiceDesign serviceDesign;

	public ModelAccount add(ModelAccount modelAccount) {

		modelAccount.setId(UUID.randomUUID().toString());
		// modelAccount.setCreatedDate(new Date().toString());
		// modelAccount.setModifiedDate(new Date().toString());

		try {
			repositoryAccount.save(modelAccount);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelAccount;
	}

	public List<ModelAccount> getList(FilterAccount filterAccount) {
		List<ModelAccount> modelAccount = new ArrayList<ModelAccount>();

		if (UtilityFunction.isNotNullAndNotEmpty(filterAccount.getCode())) {
			modelAccount = repositoryAccount.findByCode(filterAccount.getCode());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccount.getTanentId())) {
			modelAccount = repositoryAccount.findByTanentId(filterAccount.getTanentId());
		} else if (UtilityFunction.isNotNullAndNotEmpty(filterAccount.getCreatedById())) {
			modelAccount = repositoryAccount.findByCreatedById(filterAccount.getCreatedById());
		} else {
			modelAccount = repositoryAccount.findAll();
		}
		return modelAccount;
	}

	public ModelAccount create(OnboardInputModel onboardInputModel) {
		FilterDesign filterDesign = new FilterDesign();
		filterDesign.setCode("MAXISELADMIN");
		ModelDesign modelDesign = new ModelDesign();
		modelDesign = serviceDesign.getByCode(filterDesign);

		List<Step> steps = new ArrayList<Step>();
		steps = UtilityProperty.setValues(modelDesign.getSteps(), onboardInputModel.getExtendedPropertyList().getPropertyList());

		ModelAccount modelAccount = new ModelAccount();
		modelAccount.setId(UUID.randomUUID().toString());
		modelAccount.setCode(modelDesign.getCode());
		modelAccount.setCreatedDate(new Date().toString());
		modelAccount.setModifiedDate(new Date().toString());
		modelAccount.setCreatedById(modelDesign.getCreatedById());
		modelAccount.setRole(modelDesign.getRole());
		modelAccount.setRoleIdOfCreatedById(modelDesign.getRoleIdOfCreatedById());
		modelAccount.setTanentId(modelDesign.getTanentId());

		modelAccount.setSteps(steps);

		try {
			repositoryAccount.save(modelAccount);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelAccount;
	}

}
