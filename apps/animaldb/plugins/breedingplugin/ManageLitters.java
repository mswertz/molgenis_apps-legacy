/* Date:        November 15, 2010
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.3
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package plugins.breedingplugin;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.pheno.Code;
import org.molgenis.pheno.Individual;
import org.molgenis.pheno.ObservationTarget;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.protocol.ProtocolApplication;
import org.molgenis.util.Entity;
import org.molgenis.util.Tuple;

import plugins.output.LabelGenerator;
import plugins.output.LabelGeneratorException;

import commonservice.CommonService;

public class ManageLitters extends PluginModel<Entity>
{
	private static final long serialVersionUID = 7608670026855241487L;
	private List<ObservationTarget> parentgroupList;
	private List<Litter> litterList = new ArrayList<Litter>();
	private List<Litter> genoLitterList = new ArrayList<Litter>();
	private List<Litter> doneLitterList = new ArrayList<Litter>();
	private int selectedParentgroup;
	private int litter;
	private String litterName = "";
	private String datetime = "";
	private String birthdate = "";
	private String weandate = "";
	private int litterSize;
	private int weanSizeFemale;
	private int weanSizeMale;
	private boolean litterSizeApproximate;
	private CommonService ct = CommonService.getInstance();
	private SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
	private String action = "ShowLitters";
	private String nameBase = null;
	private int startNumber = -1;
	private String labelDownloadLink = null;
	private List<ObservationTarget> backgroundList;
	private List<ObservationTarget> sexList;
	private List<String> geneNameList;
	private List<String> geneStateList;
	private List<String> colorList;
	private List<Code> earmarkList;
	private int genoLitterId;
	private Database db;
	private boolean firstTime = true;
	private List<String> bases = null;

	public ManageLitters(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}
	
	public String getCustomHtmlHeaders() {
		return "<script src=\"res/scripts/custom/addingajax.js\" language=\"javascript\"></script>\n" +
				"<script src=\"res/scripts/custom/litters.js\" language=\"javascript\"></script>\n" +
				"<link rel=\"stylesheet\" style=\"text/css\" href=\"res/css/animaldb.css\">";
	}

	@Override
	public String getViewName()
	{
		return "plugins_breedingplugin_ManageLitters";
	}

	@Override
	public String getViewTemplate()
	{
		return "plugins/breedingplugin/ManageLitters.ftl";
	}
	
	// Parent group list related methods:
	public List<ObservationTarget> getParentgroupList() {
		return parentgroupList;
	}
	public void setParentgroupList(List<ObservationTarget> parentgroupList) {
		this.parentgroupList = parentgroupList;
	}
	
	public void setLitterList(List<Litter> litterList) {
		this.litterList = litterList;
	}
	public List<Litter> getLitterList() {
		return litterList;
	}
	
	public void setGenoLitterList(List<Litter> genoLitterList) {
		this.genoLitterList = genoLitterList;
	}
	public List<Litter> getGenoLitterList() {
		return genoLitterList;
	}
	
	public List<Litter> getDoneLitterList() {
		return doneLitterList;
	}

	public void setDoneLitterList(List<Litter> doneLitterList) {
		this.doneLitterList = doneLitterList;
	}

	public String getLitterName() {
		return litterName;
	}
	public void setLitterName(String litterName) {
		this.litterName = litterName;
	}
	
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdatetime(String birthdate) {
		this.birthdate = birthdate;
	}
	
	public void setWeandate(String weandate) {
		this.weandate = weandate;
	}
	public String getWeandate() {
		return weandate;
	}

	public int getLitterSize() {
		return litterSize;
	}
	public void setLitterSize(int litterSize) {
		this.litterSize = litterSize;
	}
	
	public void setWeanSizeFemale(int weanSizeFemale) {
		this.weanSizeFemale = weanSizeFemale;
	}
	public int getWeanSizeFemale() {
		return weanSizeFemale;
	}
	
	public void setWeanSizeMale(int weanSizeMale) {
		this.weanSizeMale = weanSizeMale;
	}
	public int getWeanSizeMale() {
		return weanSizeMale;
	}

	public int getSelectedParentgroup() {
		return selectedParentgroup;
	}
	public void setSelectedParentgroup(int selectedParentgroup) {
		this.selectedParentgroup = selectedParentgroup;
	}
	
	public void setLitter(int litter) {
		this.litter = litter;
	}
	public int getLitter() {
		return litter;
	}

	public boolean getLitterSizeApproximate() {
		return litterSizeApproximate;
	}
	public void setLitterSizeApproximate(boolean litterSizeApproximate) {
		this.litterSizeApproximate = litterSizeApproximate;
	}
	
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getAction()
	{
		return action;
	}

	public void setLabelDownloadLink(String labelDownloadLink) {
		this.labelDownloadLink = labelDownloadLink;
	}

	public String getLabelDownloadLink() {
		return labelDownloadLink;
	}

	public List<ObservationTarget> getBackgroundList() {
		return backgroundList;
	}

	public void setBackgroundList(List<ObservationTarget> backgroundList) {
		this.backgroundList = backgroundList;
	}

	public List<String> getGeneNameList() {
		return geneNameList;
	}

	public void setGeneNameList(List<String> geneNameList) {
		this.geneNameList = geneNameList;
	}

	public List<String> getGeneStateList() {
		return geneStateList;
	}

	public void setGeneStateList(List<String> geneStateList) {
		this.geneStateList = geneStateList;
	}
	
	public int getGenoLitterId() {
		return genoLitterId;
	}

	public void setGenoLitterId(int genoLitterId) {
		this.genoLitterId = genoLitterId;
	}

	public List<ObservationTarget> getSexList() {
		return sexList;
	}

	public void setSexList(List<ObservationTarget> sexList) {
		this.sexList = sexList;
	}

	public List<String> getColorList() {
		return colorList;
	}

	public void setColorList(List<String> colorList) {
		this.colorList = colorList;
	}

	public List<Code> getEarmarkList() {
		return earmarkList;
	}

	public void setEarmarkList(List<Code> earmarkList) {
		this.earmarkList = earmarkList;
	}

	private void setUserFields(Tuple request, boolean wean) throws Exception {
		if (wean == true) {
			if (request.getString("weandate") == null || request.getString("weandate").equals("")) {
				throw new Exception("Wean date cannot be empty");
			}
			setWeandate(request.getString("weandate"));
			setWeanSizeFemale(request.getInt("weansizefemale"));
			setWeanSizeMale(request.getInt("weansizemale"));
			
			if (request.getString("namebase") != null) {
				nameBase = request.getString("namebase");
				if (nameBase.equals("New")) {
					if (request.getString("newnamebase") != null) {
						nameBase = request.getString("newnamebase");
					} else {
						nameBase = "";
					}
				}
			} else {
				nameBase = "";
			}
			if (request.getInt("startnumber") != null) {
				startNumber = request.getInt("startnumber");
			} else {
				startNumber = 1; // standard start at 1
			}
			
		} else {
			setLitterName(request.getString("littername"));
			String parentgroupIdString = request.getString("parentgroup");
			parentgroupIdString = parentgroupIdString.replace(".", "");
			parentgroupIdString = parentgroupIdString.replace(",", "");
			setSelectedParentgroup(Integer.parseInt(parentgroupIdString));
			if (request.getString("birthdate") == null || request.getString("birthdate").equals("")) {
				throw new Exception("Birth date cannot be empty");
			}
			setBirthdatetime(request.getString("birthdate"));
			setLitterSize(request.getInt("littersize"));
			if (request.getBool("sizeapp_toggle") != null) {
				setLitterSizeApproximate(true);
			} else {
				setLitterSizeApproximate(false);
			}
		}
	}
	
	public String getParentInfo() {
		
		try {
			String returnString = "";
			
			int parentgroupId = ct.getMostRecentValueAsXref(this.getGenoLitterId(), ct.getMeasurementId("Parentgroup"));
			String parentgroupName = ct.getObservationTargetById(parentgroupId).getName();
			
			returnString += ("Parentgroup: " + parentgroupName + "<br />");
			returnString += (getLineInfo(parentgroupId) + "<br />");
			
			int motherId = findParentForParentgroup(parentgroupId, "Mother");
			returnString += ("Mother: " + getGenoInfo(motherId) + "<br />");
			int fatherId = findParentForParentgroup(parentgroupId, "Father");
			returnString += ("Father: " + getGenoInfo(fatherId) + "<br />");
			
			return returnString;
			
		} catch (Exception e) {
			return "No (complete) parent info available";
		}
	}
	
	public List<Individual> getAnimalsInLitter() {
		try {
			return getAnimalsInLitter(this.getGenoLitterId());
		} catch (Exception e) {
			// On fail, return empty list to UI
			return new ArrayList<Individual>();
		}
	}
	
	public List<Individual> getAnimalsInLitter(int litterId) {
		List<Individual> returnList = new ArrayList<Individual>();
		try {
			Query<ObservedValue> q = this.db.query(ObservedValue.class);
			q.addRules(new QueryRule(ObservedValue.RELATION, Operator.EQUALS, litterId));
			q.addRules(new QueryRule(ObservedValue.FEATURE, Operator.EQUALS, ct.getMeasurementId("Litter")));
			List<ObservedValue> valueList = q.find();
			int animalId;
			for (ObservedValue value : valueList) {
				animalId = value.getTarget_Id();
				returnList.add(ct.getIndividualById(animalId));
			}
			return returnList;
		} catch (Exception e) {
			// On fail, return empty list to UI
			return new ArrayList<Individual>();
		}
	}
	
	public int getAnimalSex(int animalId) {
		try {
			return ct.getMostRecentValueAsXref(animalId, ct.getMeasurementId("Sex"));
		} catch (Exception e) {
			return -1;
		}
	}
	
	public String getAnimalColor(int animalId) {
		try {
			return ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("Color"));
		} catch (Exception e) {
			return "unknown";
		}
	}
	
	public String getAnimalBirthDate(int animalId) {
		try {
			return ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("DateOfBirth"));
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getAnimalEarmark(int animalId) {
		try {
			return ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("Earmark"));
		} catch (Exception e) {
			return "";
		}
	}
	
	public int getAnimalBackground(int animalId) {
		try {
			return ct.getMostRecentValueAsXref(animalId, ct.getMeasurementId("Background"));
		} catch (Exception e) {
			return -1;
		}
	}
	
	public String getAnimalGeneName(int animalId) {
		try {
			return ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("GeneName"));
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getAnimalGeneState(int animalId) {
		try {
			return ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("GeneState"));
		} catch (Exception e) {
			return "";
		}
	}
	
	private int findParentForParentgroup(int parentgroupId, String parentSex) throws DatabaseException, ParseException {
		int measurementId = ct.getMeasurementId(parentSex);
		Query<ObservedValue> parentQuery = db.query(ObservedValue.class);
		parentQuery.addRules(new QueryRule(ObservedValue.RELATION, Operator.EQUALS, parentgroupId));
		parentQuery.addRules(new QueryRule(ObservedValue.FEATURE, Operator.EQUALS, measurementId));
		List<ObservedValue> parentValueList = parentQuery.find();
		if (parentValueList.size() > 0) {
			return parentValueList.get(0).getTarget_Id();
		} else {
			throw new DatabaseException("No " + parentSex + " found for parentgroup with ID " + parentgroupId);
		}
	}
	
	private String getGenoInfo(int animalId) throws DatabaseException, ParseException {
		String returnString = "";
		int measurementId = ct.getMeasurementId("Background");
		int animalBackgroundId = ct.getMostRecentValueAsXref(animalId, measurementId);
		String animalBackgroundName = "unknown";
		if (animalBackgroundId != -1) {
			animalBackgroundName = ct.getObservationTargetById(animalBackgroundId).getName();
		}
		returnString += ("background: " + animalBackgroundName);
		Query<ObservedValue> q = this.db.query(ObservedValue.class);
		q.addRules(new QueryRule(ObservedValue.TARGET, Operator.EQUALS, animalId));
		q.addRules(new QueryRule(ObservedValue.FEATURE, Operator.EQUALS, ct.getMeasurementId("GeneName")));
		List<ObservedValue> valueList = q.find();
		if (valueList != null) {
			int protocolApplicationId;
			for (ObservedValue value : valueList) {
				String geneName = value.getValue();
				String geneState = "";
				protocolApplicationId = value.getProtocolApplication_Id();
				q = this.db.query(ObservedValue.class);
				q.addRules(new QueryRule(ObservedValue.TARGET, Operator.EQUALS, animalId));
				q.addRules(new QueryRule(ObservedValue.FEATURE, Operator.EQUALS, ct.getMeasurementId("GeneState")));
				q.addRules(new QueryRule(ObservedValue.PROTOCOLAPPLICATION, Operator.EQUALS, protocolApplicationId));
				List<ObservedValue> geneStateValueList = q.find();
				if (geneStateValueList != null) {
					if (geneStateValueList.size() > 0) {
						geneState = geneStateValueList.get(0).getValue();
					}
				}
				returnString += (", gene: " + geneName + ", ");
				returnString += ("state: " + geneState);
			}
		}
		
		return returnString;
	}
	
	private String getLineInfo(int parentgroupId) throws DatabaseException, ParseException {
		int lineId = ct.getMostRecentValueAsXref(parentgroupId, ct.getMeasurementId("Line"));
		String lineName = ct.getObservationTargetById(lineId).getName();
		return ("Line: " + lineName);
	}

	public List<String> getBases() {
		return bases;
	}

	public void setBases(List<String> bases) {
		this.bases = bases;
	}
	
	public String getStartNumberHelperContent() {
		try {
			String helperContents = "";
			helperContents += (ct.getHighestNumberForNameBase("") + 1);
			helperContents += ";1";
			for (String base : this.bases) {
				if (!base.equals("")) {
					helperContents += (";" + (ct.getHighestNumberForNameBase(base) + 1));
				}
			}
			return helperContents;
		} catch (Exception e) {
			return "";
		}
	}
	
	public int getStartNumberForEmptyBase() {
		try {
			return ct.getHighestNumberForNameBase("") + 1;
		} catch (DatabaseException e) {
			return 1;
		}
	}

	@Override
	public void handleRequest(Database db, Tuple request)
	{
		try {
			Calendar calendar = Calendar.getInstance();
			Date now = calendar.getTime();
			
			this.action = request.getString("__action");
			
			if (action.equals("MakeTmpLabels")) {
				setLitter(request.getInt("id"));
				makeTempCageLabels();
			}
			
			if (action.equals("MakeDefLabels")) {
				setLitter(request.getInt("id"));
				makeDefCageLabels();
			}
			
			if (action.equals("AddLitter")) {
				//
			}
			
			if (action.equals("ShowLitters")) {
				//
			}
			
			if (action.equals("ApplyAddLitter")) {
				int invid = ct.getOwnUserInvestigationIds(this.getLogin().getUserId()).get(0);
				setUserFields(request, false);
				Date eventDate = dateOnlyFormat.parse(birthdate);
				
				// Init lists that we can later add to the DB at once
				List<ObservedValue> valuesToAddList = new ArrayList<ObservedValue>();
				
				// Make group
				int litterid = ct.makePanel(invid, litterName, this.getLogin().getUserId());
				// Mark group as a litter
				int protocolId = ct.getProtocolId("SetTypeOfGroup");
				int measurementId = ct.getMeasurementId("TypeOfGroup");
				db.add(ct.createObservedValueWithProtocolApplication(invid, now, null, 
						protocolId, measurementId, litterid, "Litter", 0));

				// Apply other fields using event
				protocolId = ct.getProtocolId("SetLitterSpecs");
				ProtocolApplication app = ct.createProtocolApplication(invid, protocolId);
				db.add(app);
				int eventid = app.getId();
				// Parentgroup
				measurementId = ct.getMeasurementId("Parentgroup");
				valuesToAddList.add(ct.createObservedValue(invid, eventid, eventDate, null, measurementId, 
						litterid, null, selectedParentgroup));
				// Date of Birth
				measurementId = ct.getMeasurementId("DateOfBirth");
				valuesToAddList.add(ct.createObservedValue(invid, eventid, eventDate, null, measurementId, 
						litterid, birthdate, 0));
				// Size
				measurementId = ct.getMeasurementId("Size");
				valuesToAddList.add(ct.createObservedValue(invid, eventid, eventDate, null, measurementId, litterid, 
						Integer.toString(litterSize), 0));
				// Size approximate (certain)?
				String valueString = "0";
				if (litterSizeApproximate == true) {
					valueString = "1";
				}
				measurementId = ct.getMeasurementId("Certain");
				valuesToAddList.add(ct.createObservedValue(invid, eventid, eventDate, null, measurementId, litterid, 
						valueString, 0));
				// Get Source via Line
				measurementId = ct.getMeasurementId("Source");
				try {
					int lineId = ct.getMostRecentValueAsXref(selectedParentgroup, ct.getMeasurementId("Line"));
					int sourceId = ct.getMostRecentValueAsXref(lineId, measurementId);
					protocolId = ct.getProtocolId("SetSource");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, 
						eventDate, null, protocolId, measurementId, litterid, null, sourceId));
				} catch(Exception e) {
					//
				}
				// Add everything to DB
				db.add(valuesToAddList);
				
				this.action = "ShowLitters";
				this.reload(db);
				this.reloadLitterLists(db);
				this.getMessages().clear();
				this.getMessages().add(new ScreenMessage("Litter succesfully added", true));
			}
			
			if (action.equals("ShowWean")) {
				// Find and set litter
				setLitter(request.getInt("id"));
			}
			
			if (action.equals("Wean")) {
				int invid = ct.getObservationTargetById(litter).getInvestigation_Id();
				setUserFields(request, true);
				Date weanDate = dateOnlyFormat.parse(weandate);
				
				// Init lists that we can later add to the DB at once
				List<ObservedValue> valuesToAddList = new ArrayList<ObservedValue>();
				List<ObservationTarget> animalsToAddList = new ArrayList<ObservationTarget>();
				
				// Source (take from litter)
				int sourceId;
				try {
					sourceId = ct.getMostRecentValueAsXref(litter, ct.getMeasurementId("Source"));
				} catch (Exception e) {
					throw(new Exception("No source found - litter not weaned"));
				}
				// Get litter birth date
				String litterBirthDateString;
				Date litterBirthDate;
				try {
					litterBirthDateString = ct.getMostRecentValueAsString(litter, ct.getMeasurementId("DateOfBirth"));
					litterBirthDate = dateOnlyFormat.parse(litterBirthDateString);
				} catch (Exception e) {
					throw(new Exception("No litter birth date found - litter not weaned"));
				}
				// Find Parentgroup for this litter
				int parentgroupId;
				try {
					parentgroupId = ct.getMostRecentValueAsXref(litter, ct.getMeasurementId("Parentgroup"));
				} catch (Exception e) {
					throw(new Exception("No parentgroup found - litter not weaned"));
				}
				// Find Line for this Parentgroup
				int lineId = ct.getMostRecentValueAsXref(parentgroupId, ct.getMeasurementId("Line"));
				// Find first mother, plus her animal type, species, color, background, gene name and gene state
				int speciesId;
				String animalType;
				String color;
				int backgroundId;
				String geneName;
				String geneState;
				try {
					int motherId = findParentForParentgroup(parentgroupId, "Mother");
					speciesId = ct.getMostRecentValueAsXref(motherId, ct.getMeasurementId("Species"));
					animalType = ct.getMostRecentValueAsString(motherId, ct.getMeasurementId("AnimalType"));
					color = ct.getMostRecentValueAsString(motherId, ct.getMeasurementId("Color"));
					backgroundId = ct.getMostRecentValueAsXref(motherId, ct.getMeasurementId("Background"));
					geneName = ct.getMostRecentValueAsString(motherId, ct.getMeasurementId("GeneName"));
					geneState = ct.getMostRecentValueAsString(motherId, ct.getMeasurementId("GeneState"));
				} catch (Exception e) {
					throw(new Exception("No mother (properties) found - litter not weaned"));
				}
				// Keep normal and transgene types, but set type of child from wild parents to normal
				if (animalType.equals("C. Wildvang") || animalType.equals("D. Biotoop")) {
					animalType = "A. Gewoon dier";
				}
				// Set wean sizes
				int weanSize = weanSizeFemale + weanSizeMale;
				int protocolId = ct.getProtocolId("SetWeanSize");
				int measurementId = ct.getMeasurementId("WeanSize");
				valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, now, null, 
						protocolId, measurementId, litter, Integer.toString(weanSize), 0));
				protocolId = ct.getProtocolId("SetWeanSizeFemale");
				measurementId = ct.getMeasurementId("WeanSizeFemale");
				valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, now, null, 
						protocolId, measurementId, litter, Integer.toString(weanSizeFemale), 0));
				// Set wean date on litter -> this is how we mark a litter as weaned (but not genotyped)
				protocolId = ct.getProtocolId("SetWeanDate");
				measurementId = ct.getMeasurementId("WeanDate");
				valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
						null, protocolId, measurementId, litter, weandate, 0));
				
				db.beginTx();
				
				// Link animals to litter and set wean dates etc.
				for (int animalNumber = 0; animalNumber < weanSize; animalNumber++) {
					ObservationTarget animalToAdd = ct.createIndividual(invid, nameBase + (startNumber + animalNumber), 
							this.getLogin().getUserId());
					animalsToAddList.add(animalToAdd);
				}
				db.add(animalsToAddList);
				
				int animalNumber = 0;
				for (ObservationTarget animal : animalsToAddList) {
					int animalId = animal.getId();
					
					// TODO: link every value to a single Wean protocol application instead of to its own one
					
					// Link to litter
					protocolId = ct.getProtocolId("SetLitter");
					measurementId = ct.getMeasurementId("Litter");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
							null, protocolId, measurementId, animalId, null, litter));
					// Set line also on animal itself
					protocolId = ct.getProtocolId("SetLine");
					measurementId = ct.getMeasurementId("Line");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
							null, protocolId, measurementId, animalId, null, lineId));
					// Set sex
					int sexId = ct.getObservationTargetId("Female");
					if (animalNumber >= weanSizeFemale) {
						sexId = ct.getObservationTargetId("Male");
					}
					protocolId = ct.getProtocolId("SetSex");
					measurementId = ct.getMeasurementId("Sex");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
							null, protocolId, measurementId, animalId, null, sexId));
					// Set wean date on animal
					protocolId = ct.getProtocolId("SetWeanDate");
					measurementId = ct.getMeasurementId("WeanDate");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
							null, protocolId, measurementId, animalId, weandate, 0));
					// Set 'Active'
					protocolId = ct.getProtocolId("SetActive");
					measurementId = ct.getMeasurementId("Active");
			 		valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, 
			 				litterBirthDate, null, protocolId, measurementId, animalId, "Alive", 0));
			 		// Set 'Date of Birth'
			 		protocolId = ct.getProtocolId("SetDateOfBirth");
					measurementId = ct.getMeasurementId("DateOfBirth");
			 		valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate,
			 				null, protocolId, measurementId, animalId, litterBirthDateString, 0));
					// Set species
			 		protocolId = ct.getProtocolId("SetSpecies");
					measurementId = ct.getMeasurementId("Species");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
							null, protocolId, measurementId, animalId, null, speciesId));
					// Set animal type
					protocolId = ct.getProtocolId("SetAnimalType");
					measurementId = ct.getMeasurementId("AnimalType");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
							null, protocolId, measurementId, animalId, animalType, 0));
					// Set source
					protocolId = ct.getProtocolId("SetSource");
					measurementId = ct.getMeasurementId("Source");
					valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
							null, protocolId, measurementId, animalId, null, sourceId));
					// Set color based on mother's (can be changed during genotyping)
					if (!color.equals("")) {
						protocolId = ct.getProtocolId("SetColor");
						measurementId = ct.getMeasurementId("Color");
						valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
								null, protocolId, measurementId, animalId, color, 0));
					}
					// Set background based on mother's (can be changed during genotyping)
					if (backgroundId != -1) {
						protocolId = ct.getProtocolId("SetBackground");
						measurementId = ct.getMeasurementId("Background");
						valuesToAddList.add(ct.createObservedValueWithProtocolApplication(invid, weanDate, 
								null, protocolId, measurementId, animalId, null, backgroundId));
					}
					if (!geneName.equals("") && !geneState.equals("")) {
						protocolId = ct.getProtocolId("SetGenotype");
						int paId = ct.makeProtocolApplication(invid, protocolId);
						// Set gene name based on mother's (can be changed during genotyping)
						measurementId = ct.getMeasurementId("GeneName");
						valuesToAddList.add(ct.createObservedValue(invid, paId, weanDate, 
								null, measurementId, animalId, geneName, 0));
						// Set gene state based on mother's (can be changed during genotyping)
						measurementId = ct.getMeasurementId("GeneState");
						valuesToAddList.add(ct.createObservedValue(invid, paId, weanDate, 
								null, measurementId, animalId, geneState, 0));
					}
					
					animalNumber++;
				}
				
				db.add(valuesToAddList);
				
				db.commitTx();
				
				// Update custom label map now new animals have been added
				ct.makeObservationTargetNameMap(this.getLogin().getUserId(), true);
				
				this.action = "ShowLitters";
				this.reload(db);
				this.reloadLitterLists(db);
				this.getMessages().add(new ScreenMessage("All " + weanSize + " animals succesfully weaned", true));
			}
			
			if (action.equals("ShowGenotype")) {
				this.setGenoLitterId(request.getInt("id"));
			}
			
			if (action.equals("Genotype")) {
				
				int invid = ct.getObservationTargetById(this.genoLitterId).getInvestigation_Id();
				List<Integer> investigationIds = ct.getAllUserInvestigationIds(this.getLogin().getUserId());
				
				// Set genotype date on litter -> this is how we mark a litter as genotyped
				// TODO: use proper date from field instead of 'weandate' which is undefined here!!
				int protocolId = ct.getProtocolId("SetGenotypeDate");
				int measurementId = ct.getMeasurementId("GenotypeDate");
				db.add(ct.createObservedValueWithProtocolApplication(invid, now, 
						null, protocolId, measurementId, this.genoLitterId, weandate, 0));
				
				int animalCount = 0;
				for (Individual animal : this.getAnimalsInLitter()) {
					
					// Here we (re)set the values from the genotyping
					
					// Set sex
					int sexId = request.getInt("sex_" + animalCount);
					ObservedValue value = ct.getObservedValuesByTargetAndFeature(animal.getId(), 
							ct.getMeasurementByName("Sex"), investigationIds, invid).get(0);
					value.setRelation_Id(sexId);
					value.setValue(null);
					if (value.getProtocolApplication_Id() == null) {
						int paId = ct.makeProtocolApplication(invid, ct.getProtocolId("SetSex"));
						value.setProtocolApplication_Id(paId);
						db.add(value);
					} else {
						db.update(value);
					}
					// Set birth date
					String dob = request.getString("dob_" + animalCount);
					value = ct.getObservedValuesByTargetAndFeature(animal.getId(), 
							ct.getMeasurementByName("DateOfBirth"), investigationIds, invid).get(0);
					value.setValue(dob);
					if (value.getProtocolApplication_Id() == null) {
						int paId = ct.makeProtocolApplication(invid, ct.getProtocolId("SetDateOfBirth"));
						value.setProtocolApplication_Id(paId);
						db.add(value);
					} else {
						db.update(value);
					}
					// Set color
					String color = request.getString("color_" + animalCount);
					value = ct.getObservedValuesByTargetAndFeature(animal.getId(), 
							ct.getMeasurementByName("Color"), investigationIds, invid).get(0);
					value.setValue(color);
					if (value.getProtocolApplication_Id() == null) {
						int paId = ct.makeProtocolApplication(invid, ct.getProtocolId("SetColor"));
						value.setProtocolApplication_Id(paId);
						db.add(value);
					} else {
						db.update(value);
					}
					// Set earmark
					String earmark = request.getString("earmark_" + animalCount);
					value = ct.getObservedValuesByTargetAndFeature(animal.getId(), 
							ct.getMeasurementByName("Earmark"), investigationIds, invid).get(0);
					value.setValue(earmark);
					if (value.getProtocolApplication_Id() == null) {
						int paId = ct.makeProtocolApplication(invid, ct.getProtocolId("SetEarmark"));
						value.setProtocolApplication_Id(paId);
						db.add(value);
					} else {
						db.update(value);
					}
					// Set background
					int backgroundId = request.getInt("background_" + animalCount);
					value = ct.getObservedValuesByTargetAndFeature(animal.getId(), 
							ct.getMeasurementByName("Background"), investigationIds, invid).get(0);
					value.setRelation_Id(backgroundId);
					value.setValue(null);
					if (value.getProtocolApplication_Id() == null) {
						int paId = ct.makeProtocolApplication(invid, ct.getProtocolId("SetBackground"));
						value.setProtocolApplication_Id(paId);
						db.add(value);
					} else {
						db.update(value);
					}
					// Set genotype
					int paId = ct.makeProtocolApplication(invid, ct.getProtocolId("SetGenotype"));
					String geneName = request.getString("geneName_" + animalCount);
					value = ct.getObservedValuesByTargetAndFeature(animal.getId(), 
							ct.getMeasurementByName("GeneName"), investigationIds, invid).get(0);
					value.setValue(geneName);
					if (value.getProtocolApplication_Id() == null) {
						value.setProtocolApplication_Id(paId);
						db.add(value);
					} else {
						db.update(value);
					}
					String geneState = request.getString("geneState_" + animalCount);
					value = ct.getObservedValuesByTargetAndFeature(animal.getId(), 
							ct.getMeasurementByName("GeneState"), investigationIds, invid).get(0);
					value.setValue(geneState);
					if (value.getProtocolApplication_Id() == null) {
						value.setProtocolApplication_Id(paId);
						db.add(value);
					} else {
						db.update(value);
					}
					
					animalCount++;
				}
				
				this.action = "ShowLitters";
				this.reload(db);
				this.reloadLitterLists(db);
				this.getMessages().add(new ScreenMessage("All " + animalCount + " animals successfully genotyped", true));
			}

		} catch (Exception e) {
			try {
				db.rollbackTx();
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
			if (e.getMessage() != null) {
				this.getMessages().clear();
				this.getMessages().add(new ScreenMessage(e.getMessage(), false));
			}
			e.printStackTrace();
			this.action = "ShowLitters";
		}
	}

	private void makeDefCageLabels() throws LabelGeneratorException, DatabaseException, ParseException {
		
		// PDF file stuff
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File pdfFile = new File(tmpDir.getAbsolutePath() + File.separatorChar + "deflabels.pdf");
		String filename = pdfFile.getName();
		LabelGenerator labelgenerator = new LabelGenerator(2);
		labelgenerator.startDocument(pdfFile);
		
		// Litter stuff
		int parentgroupId = ct.getMostRecentValueAsXref(litter, ct.getMeasurementId("Parentgroup"));
		String line = this.getLineInfo(parentgroupId);
		int motherId = findParentForParentgroup(parentgroupId, "Mother");
		String motherInfo = this.getGenoInfo(motherId);
		int fatherId = findParentForParentgroup(parentgroupId, "Father");
		String fatherInfo = this.getGenoInfo(fatherId);
		
		List<String> elementList;
		
		for (Individual animal : this.getAnimalsInLitter(litter)) {
			int animalId = animal.getId();
			elementList = new ArrayList<String>();
			// Earmark
			elementList.add("Earmark: " + ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("Earmark")));
			// Name / custom label
			elementList.add("Name: " + ct.getObservationTargetLabel(animalId));
			// Line
			elementList.add(line);
			// Background + GeneName + GeneState
			elementList.add(this.getGenoInfo(animalId));
			// Color + Sex
			String colorSex = "Color/sex: ";
			colorSex += ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("Color"));
			colorSex += "\t\t";
			int sexId = ct.getMostRecentValueAsXref(animalId, ct.getMeasurementId("Sex"));
			colorSex += ct.getObservationTargetById(sexId).getName();
			elementList.add(colorSex);
			// Birthdate
			elementList.add("Birthdate: " + ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("DateOfBirth")));
			// Geno mother
			elementList.add("Mother: " + motherInfo);
			// Geno father
			elementList.add("Father: " + fatherInfo);
			// OldUliDbExperimentator
			elementList.add("Experimenter: " + ct.getMostRecentValueAsString(animalId, ct.getMeasurementId("OldUliDbExperimentator")));
			
			labelgenerator.addLabelToDocument(elementList);
		}
		
		// In case of an odd number of animals, add extra label to make row full
		if (this.getAnimalsInLitter(litter).size() %2 != 0) {
			elementList = new ArrayList<String>();
			labelgenerator.addLabelToDocument(elementList);
		}
		
		labelgenerator.finishDocument();
		this.setLabelDownloadLink("<a href=\"tmpfile/" + filename + "\">Download definitive cage labels as pdf</a>");
	}

	private void makeTempCageLabels() throws Exception {
		
		// PDF file stuff
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File pdfFile = new File(tmpDir.getAbsolutePath() + File.separatorChar + "weanlabels.pdf");
		String filename = pdfFile.getName();
		LabelGenerator labelgenerator = new LabelGenerator(2);
		labelgenerator.startDocument(pdfFile);
		List<String> elementList;
		
		// Selected litter stuff
		int parentgroupId = ct.getMostRecentValueAsXref(litter, ct.getMeasurementId("Parentgroup"));
		int lineId = ct.getMostRecentValueAsXref(parentgroupId, ct.getMeasurementId("Line"));
		String lineName = ct.getObservationTargetById(lineId).getName();
		int motherId = findParentForParentgroup(parentgroupId, "Mother");
		String motherName = ct.getObservationTargetById(motherId).getName();
		int fatherId = findParentForParentgroup(parentgroupId, "Father");
		String fatherName = ct.getObservationTargetById(fatherId).getName();
		String litterBirthDateString = ct.getMostRecentValueAsString(litter, ct.getMeasurementId("DateOfBirth"));
		int nrOfAnimals = Integer.parseInt(ct.getMostRecentValueAsString(litter, ct.getMeasurementId("WeanSize")));
		int nrOfFemales = Integer.parseInt(ct.getMostRecentValueAsString(litter, ct.getMeasurementId("WeanSizeFemale")));
		int nrOfMales = nrOfAnimals - nrOfFemales;
		
		// Labels for females
		int nrOfCages = 0;
		while (nrOfFemales > 0) {
			elementList = new ArrayList<String>();
			// Line name + Nr. of females in cage
			String firstLine = lineName + "\t\t"; 
			// Females can be 2 or 3 in a cage, if possible not 1
			int cageSize;
			if (nrOfFemales > 4) {
				cageSize = 3;
			} else {
				if (nrOfFemales == 4) {
					cageSize = 2;
				} else {
					cageSize = nrOfFemales;
				}
			}
			firstLine += (cageSize + " female");
			if (cageSize > 1) firstLine += "s";
			elementList.add(firstLine);
			// Parents
			elementList.add(motherName + " x " + fatherName);
			// Litter birth date
			elementList.add(litterBirthDateString);
			// Nrs. for writing extra information behind
			for (int i = 1; i <= cageSize; i++) {
				elementList.add(i + ".");
			}
			
			labelgenerator.addLabelToDocument(elementList);
			nrOfFemales -= cageSize;
			nrOfCages++;
		}
		
		// Labels for males
		while (nrOfMales > 0) {
			elementList = new ArrayList<String>();
			// Line name + Nr. of males in cage
			String firstLine = lineName; 
			if (nrOfMales >= 2) {
				firstLine += "\t\t2 males";
			} else {
				firstLine += "\t\t1 male";
			}
			elementList.add(firstLine);
			// Parents
			elementList.add(motherName + " x " + fatherName);
			// Litter birth date
			elementList.add(litterBirthDateString);
			// Nrs. for writing extra information behind
			for (int i = 1; i <= Math.min(nrOfMales, 2); i++) {
				elementList.add(i + ".");
			}
			
			labelgenerator.addLabelToDocument(elementList);
			nrOfMales -= 2;
			nrOfCages++;
		}
		
		// In case of an odd number of cages, add extra label to make row full
		if (nrOfCages %2 != 0) {
			elementList = new ArrayList<String>();
			labelgenerator.addLabelToDocument(elementList);
		}
		
		labelgenerator.finishDocument();
		this.setLabelDownloadLink("<a href=\"tmpfile/" + filename + "\">Download temporary wean labels as pdf</a>");
	}

	@Override
	public void reload(Database db)
	{	
		if (firstTime == true) {
			firstTime = false;
			reloadLitterLists(db);
		}
		
		try {
			List<Integer> investigationIds = ct.getAllUserInvestigationIds(this.getLogin().getUserId());
			
			// Populate parent group list
			this.setParentgroupList(ct.getAllMarkedPanels("Parentgroup", investigationIds));
			// Populate backgrounds list
			this.setBackgroundList(ct.getAllMarkedPanels("Background", investigationIds));
			// Populate sexes list
			this.setSexList(ct.getAllMarkedPanels("Sex", investigationIds));
			// Populate gene name list
			this.setGeneNameList(ct.getAllCodesForFeatureAsStrings("GeneName"));
			// Populate gene state list
			this.setGeneStateList(ct.getAllCodesForFeatureAsStrings("GeneState"));
			// Populate color list
			this.setColorList(ct.getAllCodesForFeatureAsStrings("Color"));
			// Populate earmark list
			this.setEarmarkList(ct.getAllCodesForFeature("Earmark"));
			// Populate name bases list
			this.setBases(ct.getNameBases());
		} catch (Exception e) {
			if (e.getMessage() != null) {
				this.getMessages().clear();
				this.getMessages().add(new ScreenMessage(e.getMessage(), false));
			}
			e.printStackTrace();
		}
	}
	
	private void reloadLitterLists(Database db) {
		this.db = db;
		
		ct.setDatabase(this.db);
		ct.makeObservationTargetNameMap(this.getLogin().getUserId(), false);
		
		try {
			List<Integer> investigationIds = ct.getAllUserInvestigationIds(this.getLogin().getUserId());
			
			// Populate litter lists
			litterList.clear();
			genoLitterList.clear();
			doneLitterList.clear();
			
			// Make list of ID's of weaned litters
			List<Integer> weanedLitterIdList = new ArrayList<Integer>();
			int featid = ct.getMeasurementId("WeanDate");
			Query<ObservedValue> q = db.query(ObservedValue.class);
			q.addRules(new QueryRule(ObservedValue.FEATURE, Operator.EQUALS, featid));
			List<ObservedValue> valueList = q.find();
			for (ObservedValue value : valueList) {
				int litterId = value.getTarget_Id();
				if (!weanedLitterIdList.contains(litterId)) {
					weanedLitterIdList.add(litterId);
				}
			}
			// Make list of ID's of genotyped litters
			List<Integer> genotypedLitterIdList = new ArrayList<Integer>();
			featid = ct.getMeasurementId("GenotypeDate");
			q = db.query(ObservedValue.class);
			q.addRules(new QueryRule(ObservedValue.FEATURE, Operator.EQUALS, featid));
			valueList = q.find();
			for (ObservedValue value : valueList) {
				int litterId = value.getTarget_Id();
				if (!genotypedLitterIdList.contains(litterId)) {
					genotypedLitterIdList.add(litterId);
				}
			}
			// Get all litters that the current user has rights on
			List<ObservationTarget> allLitterList = ct.getAllMarkedPanels("Litter", investigationIds);
			for (ObservationTarget litter : allLitterList) {
				int litterId = litter.getId();
				// Make a temporary litter and set all relevant values
				Litter litterToAdd = new Litter();
				// ID
				litterToAdd.setId(litterId);
				// Name
				litterToAdd.setName(litter.getName());
				// Parentgroup
				featid = ct.getMeasurementId("Parentgroup");
				int parentgroupId = ct.getMostRecentValueAsXref(litterId, featid);
				String parentgroup = ct.getObservationTargetById(parentgroupId).getName();
				litterToAdd.setParentgroup(parentgroup);
				// Birth date
				featid = ct.getMeasurementId("DateOfBirth");
				String birthDate = ct.getMostRecentValueAsString(litterId, featid);
				if (!birthDate.equals("")) {
					litterToAdd.setBirthDate(birthDate);
				}
				// Wean date
				featid = ct.getMeasurementId("WeanDate");
				String weanDate = ct.getMostRecentValueAsString(litterId, featid);
				if (weanDate != null && !weanDate.equals("")) {
					litterToAdd.setWeanDate(weanDate);
				}
				// Size
				featid = ct.getMeasurementId("Size");
				String size = ct.getMostRecentValueAsString(litterId, featid);
				if (size.equals("")) {
					litterToAdd.setSize(-1);
				} else {
					litterToAdd.setSize(Integer.parseInt(size));
				}
				// Wean size
				featid = ct.getMeasurementId("WeanSize");
				String weanSize = ct.getMostRecentValueAsString(litterId, featid);
				if (weanSize.equals("")) {
					litterToAdd.setWeanSize(-1);
				} else {
					litterToAdd.setWeanSize(Integer.parseInt(weanSize));
				}
				// Size approximate
				String isApproximate = "";
				featid = ct.getMeasurementId("Certain");
				String tmpValue = ct.getMostRecentValueAsString(litterId, featid);
				if (tmpValue.equals("0")) {
					isApproximate = "No";
				}
				if (tmpValue.equals("1")) {
					isApproximate = "Yes";
				}
				litterToAdd.setSizeApproximate(isApproximate);
				// Add to the right list
				if (!weanedLitterIdList.contains(litterId)) {
					litterList.add(litterToAdd);
				} else {
					if (!genotypedLitterIdList.contains(litterId)) {
						genoLitterList.add(litterToAdd);
					} else {
						doneLitterList.add(litterToAdd);
					}
				}
			}
			
		} catch (Exception e) {
			if (e.getMessage() != null) {
				this.getMessages().clear();
				this.getMessages().add(new ScreenMessage(e.getMessage(), false));
			}
			e.printStackTrace();
		}
	}
	
}
