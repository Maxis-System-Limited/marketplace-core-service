package maxis;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import maxis.common.model.Context;
import maxis.common.model.Property;
import maxis.common.model.Role;
import maxis.common.model.Step;
import maxis.configuration.service.ServiceConfiguration;
import maxis.design.model.ModelDesign;
import maxis.design.service.ServiceDesign;
import maxis.district.model.DistrictInputModel;
import maxis.district.model.DistrictViewModel;
import maxis.district.service.ServiceDistrict;
import maxis.route.model.Edge;
import maxis.route.model.Route;
import maxis.route.service.ServiceRoute;
import maxis.thana.model.ThanaInputModel;
import maxis.thana.model.ThanaViewModel;
import maxis.thana.service.ServiceThana;

@Component
public class ApplicationRunner implements CommandLineRunner {
	
	@Autowired
	private ServiceDesign  serviceDesign;
	
	@Autowired
	private ServiceDistrict serviceDistrict;
	
	@Autowired
	private ServiceThana serviceThana;
	
	
	@Autowired
	private ServiceRoute serviceRoute;
	@Autowired
	private ServiceConfiguration serviceConfiguration;
	
	
	

	@Override
	public void run(String... args) throws Exception {
		//feedDesign();
		//addDistrictThana();
		//serviceConfiguration.get_W_From_ThanaList("LPShahadat","BAKERGANJ");
		
		List<Route> routes = new ArrayList<>();
		routes = serviceRoute.findDeliveryRoute("MAXISELLP",20,24);
		for(Route route : routes) {
			System.out.println("ROUTE ORDER: "+route.getOrderNo()+" - ROUTE NAME: "+route.getModelRoute().getName());
			for(Edge edge : route.getEdges()) {
				System.out.println("From: "+edge.getSourceNode()+" To: "+edge.getDestinationNode());
			}
		}
		
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void addDistrictThana() {
		String districtName = "";
		String districtId = "";
		int columnNo = 1;
		try {
			File districtThanaList = getFile();
			System.out.println("district-thana-list.xlsx: " + districtThanaList.getAbsolutePath() + ": "
					+ districtThanaList.exists());

			FileInputStream file = new FileInputStream(districtThanaList);

			// Create Workbook instance holding reference to .xlsx file
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns

				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:

						break;
					case Cell.CELL_TYPE_STRING:

						if (!districtName.equals(cell.getStringCellValue()) && columnNo == 1) {
							districtId = addDistrict(cell.getStringCellValue());
							districtName = cell.getStringCellValue();
							columnNo = columnNo + 1;

						} else if (columnNo != 1) {

							addThana(cell.getStringCellValue(), districtId, districtName);
							columnNo = 1;

						} else {
							columnNo = columnNo + 1;
						}

						break;
					}
				}
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getFile() throws URISyntaxException {
		URL resource = getClass().getClassLoader().getResource("district-thana-list.xlsx");
		if (resource == null) {
			throw new IllegalArgumentException("file not found!");
		} else {

			// failed if files have whitespaces or special characters
			// return new File(resource.getFile());

			return new File(resource.toURI());
		}
	}
	
	private String addDistrict(String name) {
		DistrictInputModel districtInputModel = new DistrictInputModel();
		DistrictViewModel districtViewModel = new DistrictViewModel();
		districtInputModel.setCode(name.replaceAll("\\s", ""));
		districtInputModel.setCreatedBy("SYSTEM");
		districtInputModel.setDisplayName(name);
		districtInputModel.setModifiedBy("SYSTEM");
		districtInputModel.setName(name);
		districtViewModel = serviceDistrict.add(districtInputModel);
		return districtViewModel.getId();
	}

	private String addThana(String name, String districtId, String districtName) {
		ThanaInputModel thanaInputModel = new ThanaInputModel();
		ThanaViewModel thanaViewModel = new ThanaViewModel();
		thanaInputModel.setCode(name.replaceAll("\\s", ""));
		thanaInputModel.setCreatedBy("SYSTEM");
		thanaInputModel.setDisplayName(name);
		thanaInputModel.setDistrictId(districtId);
		thanaInputModel.setModifiedBy("SYSTEM");
		thanaInputModel.setName("("+districtName+") "+name);

		thanaViewModel = serviceThana.add(thanaInputModel);
		return thanaViewModel.getId();

	}
	
	public void feedDesign() {
		//Maxis EL Admin
		List<Property> propertyList = new ArrayList<Property>();
		List<Context> contextList = new ArrayList<Context>();
		Context context = new Context();
		Step step = new Step();
		List<Step> steps = new ArrayList<Step>();
		
		
		Property property = new Property();
		property.setId(UUID.randomUUID().toString());
		property.setPropertyCode("");
		property.setPropertyValue("");
		property.setDisplayName("");
		property.setIsHidden(false);
		property.setSerialNo("");
		property.setValue("");
		
		
		Property property1 = new Property();
		property1.setId(UUID.randomUUID().toString());
		property.setPropertyCode("");
		property.setPropertyValue("");
		property1.setDisplayName("User Id");
		property1.setIsHidden(false);
		property1.setSerialNo("1");
		property1.setValue("");
		property1.setType1("String");
		property1.setType2("");
		propertyList.add(property1);
		
		
		Property property2 = new Property();
		property2.setId(UUID.randomUUID().toString());
		property.setPropertyCode("");
		property.setPropertyValue("");
		property2.setDisplayName("Password");
		property2.setIsHidden(false);
		property2.setSerialNo("2");
		property2.setValue("");
		property2.setType1("String");
		property2.setType2("");
		propertyList.add(property2);
		
		Property property3 = new Property();
		property3.setId(UUID.randomUUID().toString());
		property.setPropertyCode("");
		property.setPropertyValue("");
		property3.setDisplayName("User Name");
		property3.setIsHidden(false);
		property3.setSerialNo("3");
		property3.setType1("String");
		property3.setValue("");
		property3.setType2("");
		propertyList.add(property3);
		
		Property property4 = new Property();
		property4.setId(UUID.randomUUID().toString());
		property.setPropertyCode("");
		property.setPropertyValue("");
		property4.setDisplayName("Phone");
		property4.setIsHidden(false);
		property4.setSerialNo("4");
		property4.setValue("");
		property4.setType1("String");
		property4.setType2("");
		propertyList.add(property4);
		
		Property property5 = new Property();
		property5.setId(UUID.randomUUID().toString());
		property.setPropertyCode("");
		property.setPropertyValue("");
		property5.setDisplayName("Email");
		property5.setIsHidden(false);
		property5.setSerialNo("5");
		property5.setValue("");
		property5.setType1("String");
		property5.setType2("");
		propertyList.add(property5);
		
		Property property6 = new Property();
		property6.setId(UUID.randomUUID().toString());
		property.setPropertyCode("");
		property.setPropertyValue("");
		property6.setDisplayName("Role");
		property6.setIsHidden(false);
		property6.setSerialNo("6");
		property6.setValue("");
		property6.setType1("String");
		property6.setType2("");
		propertyList.add(property6);
		
		context.setDisplayName("Sign Up Form");
		context.setCode("SIGNUPFORM");
		context.setId(UUID.randomUUID().toString());
		context.setIsHidden(false);
		context.setProperties(propertyList);
		context.setSerialNo("1");
		context.setType("String");
		
		contextList.add(context);
		
		
		step.setContext(contextList);
		step.setDisplayName("Sign Up");
		step.setId(UUID.randomUUID().toString());
		step.setSerialNo("1");
		step.setType("String");
		
		steps.add(step);
		
		Role maxissuper = new Role();
		maxissuper.setCode("MAXISELADMIN");
		maxissuper.setRoleId("0101");   // level 00 id 00
		maxissuper.setCreatedAt("06-05-2021");
		maxissuper.setCreatedBy("System");
		maxissuper.setDisplayName("Maxis Admin");
		maxissuper.setModifiedAt("06-05-2021");
		maxissuper.setModifiedBy("System");
		maxissuper.setParentId("root");
		maxissuper.setStatus("Active");
		maxissuper.setLevel("1");
		
		ModelDesign modelDesign = new ModelDesign();
		modelDesign.setCode("MAXISELADMIN");
		modelDesign.setCreatedById("SYSTEM");
		modelDesign.setCreatedDate(new Date().toString());
		modelDesign.setId(UUID.randomUUID().toString());
		modelDesign.setModifiedById("SYSTEM");
		modelDesign.setModifiedDate(new Date().toString());
		modelDesign.setRole(maxissuper);
		modelDesign.setRoleIdOfCreatedById("1");
		modelDesign.setTanentId("MAXIS");
		modelDesign.setSteps(steps);
		
		serviceDesign.add(modelDesign);
	}

	

}
