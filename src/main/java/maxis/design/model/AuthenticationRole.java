package maxis.design.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRole {
	private String roleId;
	private String parentId;
	private String code;
	private String displayName;
	private String status;
	private String level;
	private String createdBy;
	private String createdAt;
	private String modifiedBy;
	
	public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
	
	
}



