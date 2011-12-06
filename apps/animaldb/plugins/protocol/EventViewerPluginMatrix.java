/* Date:        February 10, 2010
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package plugins.protocol;

import java.util.ArrayList;
import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.ui.GenericPlugin;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.framework.ui.html.ActionInput;
import org.molgenis.framework.ui.html.Container;
import org.molgenis.framework.ui.html.DivPanel;
import org.molgenis.framework.ui.html.Table;
import org.molgenis.matrix.component.MatrixViewer;
import org.molgenis.matrix.component.SliceablePhenoMatrix;
import org.molgenis.matrix.component.general.MatrixQueryRule;
import org.molgenis.pheno.Individual;
import org.molgenis.pheno.Measurement;
import org.molgenis.pheno.ObservationElement;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.protocol.ProtocolApplication;
import org.molgenis.util.Entity;
import org.molgenis.util.Tuple;

import commonservice.CommonService;

public class EventViewerPluginMatrix extends GenericPlugin
{
	private static final long serialVersionUID = 8804579908239186037L;
	MatrixViewer targetMatrixViewer = null;
	static String TARGETMATRIX = "targetmatrix";
	private Container container = null;
	private DivPanel div = null;
	private CommonService cs = CommonService.getInstance();
	Table animalInfo = null;
	
	public EventViewerPluginMatrix(String name, ScreenController<?> parent)
	{
		super(name, parent);
	}

	@Override
	public void handleRequest(Database db, Tuple request)
	{
		cs.setDatabase(db);
		if (targetMatrixViewer != null) {
			targetMatrixViewer.setDatabase(db);
		}
		
		String action = request.getAction();
		
		try {
			if (action.startsWith(targetMatrixViewer.getName())) {
	    		targetMatrixViewer.handleRequest(db, request);
			}
			
			if (action.equals("Select")) {
				List<?> rows = targetMatrixViewer.getSelection(db);
				int row = request.getInt(TARGETMATRIX + "_selected");
				int animalId = ((ObservationElement) rows.get(row)).getId();
				createInfoTable(db, animalId); // table gets added to screen on reload()
			}
		} catch(Exception e) {
			e.printStackTrace();
			this.getMessages().add(new ScreenMessage("Something went wrong while handling request: " + e.getMessage(), false));
		}
	}

	private void createInfoTable(Database db, int animalId) throws DatabaseException {
		List<ObservedValue> valList = db.find(ObservedValue.class, new QueryRule(ObservedValue.TARGET, 
				Operator.EQUALS, animalId));
		if (!valList.isEmpty()) {
			animalInfo = new Table("AnimalInfoTable");
			animalInfo.addColumn(""); // start time
			animalInfo.addColumn(""); // end time
			animalInfo.addColumn("Measurement");
			animalInfo.addColumn("Value");
			int rowCount = 0;
			for (ObservedValue currentValue : valList) {
				// Get the corresponding protocol (application):
				if (currentValue.getProtocolApplication_Id() != null) {
					int eventId = currentValue.getProtocolApplication_Id();
					ProtocolApplication currentEvent = db.find(ProtocolApplication.class, 
							new QueryRule(ProtocolApplication.ID, Operator.EQUALS, eventId)).get(0);
					animalInfo.addRow(currentEvent.getProtocol_Name());
				}
				if (currentValue.getTime() != null) {
					animalInfo.setCell(0, rowCount, " Valid from " + currentValue.getTime().toString());
				}
				if (currentValue.getEndtime() != null) {
					animalInfo.setCell(1, rowCount, " through " + currentValue.getEndtime().toString());
				}
				// Feature name
				String featureName = currentValue.getFeature_Name();
				if (featureName == null) {
					animalInfo.setCell(2, rowCount, "");
				} else {
					animalInfo.setCell(2, rowCount, featureName);
					// The actual value
					String currentValueContents = "";
					// Find out what the unit is:
					int featureId = currentValue.getFeature_Id();
					Measurement currentFeature = db.find(Measurement.class, 
							new QueryRule(Measurement.ID, Operator.EQUALS, featureId)).get(0);
					if (currentFeature.getDataType().equals("xref")) {
						try {
							currentValueContents = currentValue.getRelation_Name();
						} catch(Exception e) {
							int relationId = currentValue.getRelation_Id();
							currentValueContents = "Value (Target with ID " + relationId + ") not found in database";
						}
					} else {
						currentValueContents = currentValue.getValue();
					}
					animalInfo.setCell(3, rowCount, currentValueContents);
				}
				rowCount++;
			}
		}
	}

	@Override
	public void reload(Database db)
	{
		cs.setDatabase(db);
		
		if (container == null) {
			container = new Container();
			div = new DivPanel();
			try {
				List<String> investigationNames = cs.getAllUserInvestigationNames(this.getLogin().getUserId());
				List<String> measurementsToShow = new ArrayList<String>();
				measurementsToShow.add("Active"); // TODO: find a way to show zero measurements instead of one or more or ALL
				List<MatrixQueryRule> filterRules = new ArrayList<MatrixQueryRule>();
				filterRules.add(new MatrixQueryRule(MatrixQueryRule.Type.rowHeader, Individual.INVESTIGATION_NAME, 
						Operator.IN, investigationNames));
				targetMatrixViewer = new MatrixViewer(this, TARGETMATRIX, 
						new SliceablePhenoMatrix(Individual.class, Measurement.class), 
						true, false, filterRules, new MatrixQueryRule(MatrixQueryRule.Type.colHeader, Measurement.NAME, 
								Operator.IN, measurementsToShow));
				targetMatrixViewer.setDatabase(db);
				targetMatrixViewer.setLabel("Choose animal:");
				div.add(targetMatrixViewer);
				
				ActionInput selectButton = new ActionInput("Select", "", "Select");
				div.add(selectButton);
				
				container.add(div);
			} catch(Exception e) {
				e.printStackTrace();
				this.getMessages().add(new ScreenMessage("Something went wrong while loading matrix: " + e.getMessage(), false));
			}
		} else {
			targetMatrixViewer.setDatabase(db);
			if (animalInfo != null) {
				div.remove(animalInfo);
				div.add(animalInfo);
			}
		}
    }
	
	public String render()
    {
    	return container.toHtml();
    }
	
}