package maxis.jobmanagement.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maxis.jobmanagement.model.StatusLog;
import maxis.jobmanagement.viewmodel.StatusLogListViewModel;

@Service
public class StatusLogService {
	@Autowired
	private StatusLogDomainService statusLogDomainService;

	public StatusLogListViewModel getStatusLogByInstanceById(String instanceId) {
		StatusLogListViewModel statusLogListViewModel = new StatusLogListViewModel();
		try {
			statusLogListViewModel.setData(new ArrayList<StatusLog>());
			statusLogListViewModel.setData(statusLogDomainService.getStatusLogByInstanceById(instanceId));
			statusLogListViewModel.setStatus("SUCCESS");
			statusLogListViewModel.setMessage(statusLogListViewModel.getData().size() + "  Status Log Retrived.");
			return statusLogListViewModel;
		} catch (Exception e) {
			
			statusLogListViewModel.setStatus("FAILED");
			statusLogListViewModel.setMessage("Message: "+e.getMessage() + ". Cause:"+e.getCause());
			return statusLogListViewModel;
		}
	}
}
