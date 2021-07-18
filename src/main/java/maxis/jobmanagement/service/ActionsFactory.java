package maxis.jobmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.jobmanagement.inputmodel.JobStateModificationInputModel;
import maxis.jobmanagement.viewmodel.JobViewModel;

@Service
public class ActionsFactory {
	@Autowired
	private CommonAction commonAction;

	@Autowired
	private CustomAssignOneBySystem customAssignOneBySystem;

	public JobViewModel modifyJob(JobStateModificationInputModel jobStateModificationInputModel) {
		String roleActionCode = jobStateModificationInputModel.getRoleCode()
				+ jobStateModificationInputModel.getActionEventId();
		try {
			
			if (roleActionCode.equals("MAXISELSUPPORT102") || roleActionCode.equals("SYSTEM102")) {
				return customAssignOneBySystem.performActions(jobStateModificationInputModel);
			} else if (roleActionCode.equals("")) {

			} else {
				return commonAction.performActions(jobStateModificationInputModel);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
