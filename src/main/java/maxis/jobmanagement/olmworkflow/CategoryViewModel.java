package maxis.jobmanagement.olmworkflow;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.*;

@Getter
@Setter
public class CategoryViewModel {
	private Long id;
	private String name;
	private String code;
	private String describtion;
	private String createdBy;
	private String createdAt;
	private String modifiedBy;
	private String modifiedAt;
	private List<Long> categoryStates;
	private Long roleId;
	private Long classTypeId;
	private List<CategoryStateViewModel> categoryStateViewModel;
	
	public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
