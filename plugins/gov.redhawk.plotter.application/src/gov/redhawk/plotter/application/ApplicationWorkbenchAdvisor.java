/**
 * REDHAWK HEADER
 *
 * Identification: $Revision: 7884 $
 */
package gov.redhawk.plotter.application;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		final IWorkbenchConfigurer workbenchConfigurer = configurer.getWorkbenchConfigurer();

		// Force intro to NOT show
		workbenchConfigurer.setData("introOpened", Boolean.TRUE);

		workbenchConfigurer.setExitOnLastWindowClose(true);
		workbenchConfigurer.setSaveAndRestore(false);
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return PlotterPerspective.ID;
	}

}
