/* Date:        February 23, 2010
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package org.molgenis.mutation.ui.upload;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.molgenis.auth.service.MolgenisUserService;
import org.molgenis.framework.db.Database;
import org.molgenis.framework.server.MolgenisRequest;
import org.molgenis.framework.ui.EasyPluginController;
import org.molgenis.framework.ui.FreemarkerView;
import org.molgenis.framework.ui.ScreenController;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.framework.ui.ScreenView;
import org.molgenis.framework.ui.html.HiddenInput;
import org.molgenis.framework.ui.html.SelectInput;
import org.molgenis.framework.ui.html.StringInput;
import org.molgenis.mutation.ServiceLocator;
import org.molgenis.mutation.dto.VariantDTO;
import org.molgenis.mutation.service.SearchService;
import org.molgenis.mutation.service.UploadService;
import org.molgenis.mutation.ui.upload.form.BatchForm;
import org.molgenis.util.ValueLabel;
import org.molgenis.variant.Patient;

public class Upload extends EasyPluginController<UploadModel>
{

	private static final long serialVersionUID = -3499931124766785979L;
	private final transient Logger logger = Logger.getLogger(Upload.class.getSimpleName());
	private transient MolgenisUserService molgenisUserService;
	private transient UploadService uploadService;

	public Upload(String name, ScreenController<?> parent)
	{
		super(name, parent);
		this.setModel(new UploadModel(this));
		this.setView(new FreemarkerView("uploadBatch.ftl", getModel()));
		this.uploadService = ServiceLocator.instance().getUploadService();
		this.molgenisUserService = ServiceLocator.instance().getMolgenisUserService();

		this.populateBatchForm();
	}

	private ScreenView view;

	@Override
	public ScreenView getView()
	{
		return view;
	}

	public void setView(ScreenView view)
	{
		this.view = view;
	}

	@Override
	public Show handleRequest(Database db, MolgenisRequest request, OutputStream out)
	{
		try
		{
			this.uploadService.setDatabase(db);
			this.molgenisUserService.setDatabase(db);

			String action = request.getAction();

			if (StringUtils.equals(action, "newBatch"))
			{
				this.handleNewBatch();
			}
			// else if (StringUtils.equals(action, "checkBatch"))
			// {
			// this.handleCheckBatch(request);
			// }
			else if (StringUtils.equals(action, "insertBatch"))
			{
				this.handleInsertBatch(request);
			}
			else if (StringUtils.equals(action, "emailBatch"))
			{
				this.handleEmailBatch(request);
			}
			else if (StringUtils.equals(action, "newPatient"))
			{
				this.handleNewPatient();
			}
			else if (StringUtils.equals(action, "insertPatient"))
			{
				this.handleInsertPatient();
			}
			else if (StringUtils.equals(action, "newMutation"))
			{
				this.handleNewMutation();
			}
			else if (StringUtils.equals(action, "assignMutation"))
			{
				this.handleAssignMutation();
			}
			else if (StringUtils.equals(action, "checkMutation"))
			{
				this.handleCheckMutation();
			}
			else if (StringUtils.equals(action, "insertMutation"))
			{
				this.handleInsertMutation();
			}
			else if (StringUtils.equals(action, "reindex"))
			{
				this.handleReindex();
			}

			if (!this.getApplicationController().getLogin().isAuthenticated())
			{
				this.view = new FreemarkerView("uploadLogin.ftl", getModel());
			}
		}
		catch (Exception e)
		{
			// String message =
			// "Oops, an error occurred. We apologize and will work on fixing it as soon as possible. <a href=\"molgenis.do?__target=Upload&__action=newBatch\">Return to home page</a>";
			this.getModel().getMessages().add(new ScreenMessage(e.getMessage(), false));
			logger.error(e.getMessage());
			for (StackTraceElement el : e.getStackTrace())
				logger.error(el.toString());
		}
		return Show.SHOW_MAIN;
	}

	private void handleNewBatch()
	{
		this.populateBatchForm();

		this.setView(new FreemarkerView("uploadBatch.ftl", getModel()));
	}

	private void handleInsertMutation()
	{
		// TODO: Insert and mark as uncurated
		try
		{
			// this.mutationService.insert(this.getModel().getMutationUploadVO());
			String emailContents = "New mutation upload:\n" + this.getModel().getMutationUploadVO().toString()
					+ "\nUser: " + this.getApplicationController().getLogin().getUserName() + "\n";
			String adminEmail = this.molgenisUserService.findAdminEmail();
			// assuming: 'encoded' p.w. (setting deObf = true)
			this.getEmailService().email("New mutation upload for COL7A1", emailContents, adminEmail, true);

			this.getModel().getMessages().add(new ScreenMessage("Mutation successfully inserted", true));
		}
		catch (Exception e)
		{
			this.getModel().getMessages().add(new ScreenMessage(e.getMessage(), false));
		}
	}

	private void handleCheckMutation()
	{
		// TODO: implement check screen
	}

	private void handleAssignMutation()
	{
		uploadService.assignValuesFromPosition(this.getModel().getMutationUploadVO());
		this.populateMutationForm();
	}

	private void handleNewMutation()
	{
		this.populateMutationForm();
	}

	private void handleInsertPatient()
	{
		// TODO: Insert and mark as uncurated
		try
		{
			// this.patientService.insert(this.getModel().getPatientSummaryVO());
			String emailContents = "New patient upload:\n" + this.getModel().getPatientSummaryVO().toString()
					+ "\nUser: " + this.getApplicationController().getLogin().getUserName() + "\n";
			String adminEmail = this.molgenisUserService.findAdminEmail();
			// assuming: 'encoded' p.w. (setting deObf = true)
			this.getEmailService().email("New patient upload for deb-central", emailContents, adminEmail, true);
			this.getModel().getMessages().add(new ScreenMessage("Patient successfully inserted", true));
		}
		catch (Exception e)
		{
			this.getModel().getMessages().add(new ScreenMessage(e.getMessage(), false));
		}
	}

	private void handleNewPatient()
	{
		this.populatePatientForm();
	}

	private void handleEmailBatch(MolgenisRequest request)
	{
		try
		{
			File file = request.getFile("filefor_upload");

			File dest = File.createTempFile("molgenis_upload", ".xls");
			FileUtils.copyFile(file, dest);

			String emailContents = "New data upload by User: "
					+ this.getApplicationController().getLogin().getUserName() + "\n";
			String adminEmail = this.molgenisUserService.findAdminEmail();
			// assuming: 'encoded' p.w. (setting deObf = true)
			this.getEmailService().email("New data upload for COL7A1", emailContents, adminEmail,
					dest.getAbsolutePath(), true);
			this.getModel()
					.getMessages()
					.add(new ScreenMessage(
							"Thank you for your submission. Your data has been successfully emailed to us.", true));

			this.setView(new FreemarkerView("uploadBatch.ftl", getModel()));
		}
		catch (Exception e)
		{
			this.getModel().getMessages().add(new ScreenMessage(e.getMessage(), false));
		}
	}

	private void handleInsertBatch(MolgenisRequest request)
	{
		int count = uploadService.insert(request.getFile("filefor_upload"));
		this.getModel().getMessages().add(new ScreenMessage("Successfully inserted " + count + " rows", true));

		this.setView(new FreemarkerView("uploadBatch.ftl", getModel()));
	}

	private void handleReindex()
	{
		uploadService.reindex();
		this.getModel().getMessages().add(new ScreenMessage("Successfully rebuilt the full text index", true));
	}

	private void populateBatchForm()
	{
		this.getModel().setBatchForm(new BatchForm());
		((HiddenInput) this.getModel().getBatchForm().get("__target")).setValue(this.getName());
		((HiddenInput) this.getModel().getBatchForm().get("select")).setValue(this.getName());
		((HiddenInput) this.getModel().getBatchForm().get("__action")).setValue("emailBatch");
		if (this.getApplicationController().getLogin() != null)
		{
			if (StringUtils.equals(this.getApplicationController().getLogin().getUserName(), "admin"))
			{
				((HiddenInput) this.getModel().getBatchForm().get("__action")).setValue("insertBatch");
			}
		}
	}

	private void populatePatientForm()
	{
		SearchService searchService = (SearchService) ServiceLocator.instance().getService("searchService");

		List<ValueLabel> mutationOptions = new ArrayList<ValueLabel>();
		for (VariantDTO variantDTO : searchService.getAllVariants())
			mutationOptions.add(new ValueLabel(variantDTO.getId(), variantDTO.getCdnaNotation()));

		((StringInput) this.getModel().getPatientForm().get("identifier")).setValue(this.getModel()
				.getPatientSummaryVO().getPatientIdentifier());
		((SelectInput) this.getModel().getPatientForm().get("mutation1")).setOptions(mutationOptions);
		if (this.getModel().getPatientSummaryVO().getVariantDTOList().size() > 0) ((SelectInput) this.getModel()
				.getPatientForm().get("mutation1")).setValue(this.getModel().getPatientSummaryVO().getVariantDTOList()
				.get(0).getId());
		((SelectInput) this.getModel().getPatientForm().get("mutation2")).setOptions(mutationOptions);
		if (this.getModel().getPatientSummaryVO().getVariantDTOList().size() > 1) ((SelectInput) this.getModel()
				.getPatientForm().get("mutation2")).setValue(this.getModel().getPatientSummaryVO().getVariantDTOList()
				.get(1).getId());
		((StringInput) this.getModel().getPatientForm().get("number")).setValue(this.getModel().getPatientSummaryVO()
				.getPatientLocalId());
		// ((SelectInput)
		// this.getModel().getPatientForm().get("phenotype")).setOptions(phenotypeOptions);
		((SelectInput) this.getModel().getPatientForm().get("phenotype")).setValue(this.getModel()
				.getPatientSummaryVO().getPhenotypeId());
		((SelectInput) this.getModel().getPatientForm().get("phenotype")).setOnchange("toggleForm(this.value);");
		((SelectInput) this.getModel().getPatientForm().get("consent")).setOptions(new Patient().getConsentOptions());
	}

	private void populateMutationForm()
	{
		// TODO: implement
	}

	/**
	 * If not authenticated, display the "Please login" screen If authenticated,
	 * switch to the "Upload batch" screen
	 */
	@Override
	public void reload(Database db)
	{
		if (!this.getApplicationController().getLogin().isAuthenticated())
		{
			this.view = new FreemarkerView("uploadLogin.ftl", getModel());
		}
		else if ((((FreemarkerView) view).getTemplatePath()).equals("uploadLogin.ftl"))
		{
			this.view = new FreemarkerView("uploadBatch.ftl", getModel());
		}
	}
}