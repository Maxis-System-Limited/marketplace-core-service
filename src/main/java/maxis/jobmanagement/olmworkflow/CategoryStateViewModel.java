package maxis.jobmanagement.olmworkflow;

public class CategoryStateViewModel {
	private Long stateId;
	private String actionEventType;
	private Long actionId;
	private String actionName;
	private String code;
	
	
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public String getActionEventType() {
		return actionEventType;
	}
	public void setActionEventType(String actionEventType) {
		this.actionEventType = actionEventType;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
