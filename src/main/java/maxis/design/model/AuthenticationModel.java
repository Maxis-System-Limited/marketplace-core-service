package maxis.design.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;
import maxis.common.SelfValidating;

@Setter
@Getter
public class AuthenticationModel extends SelfValidating<AuthenticationModel> {

	private String userCode;
	private String userName;
	private String password;
	private String correlationId;
	private String description;
	@NotNull
	private String status;
	private String tanentId;
	private String createdBy;
	private String modifiedBy;
	private List<AuthenticationRole> roles;
	
	
	
	
	public AuthenticationModel() {
		super();
		this.setStatus("");
		this.validateSelf();
	}
	

	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}




	

}
