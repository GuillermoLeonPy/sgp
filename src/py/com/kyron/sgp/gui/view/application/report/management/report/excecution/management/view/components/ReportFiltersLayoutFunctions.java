package py.com.kyron.sgp.gui.view.application.report.management.report.excecution.management.view.components;

import java.io.IOException;

import javax.print.PrintException;

import com.vaadin.ui.Button;

import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.gui.view.application.report.management.report.excecution.management.view.components.ReportFiltersLayout.FilterValues;
import py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components.ReportProgramListLayout.ReportProgram;

public interface ReportFiltersLayoutFunctions {

	public void queryReport(final FilterValues filterValues, final Button downloadButton) throws PmsServiceException, PrintException, IOException;
	public void navigateToCallerView();
	public void setUpReportProgramFlags(final ReportProgram vReportProgram);
	public void setUpDownloadButton(final Button downloadButton);
}
