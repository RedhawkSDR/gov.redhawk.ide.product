/**
 * REDHAWK HEADER
 *
 * Identification: $Revision: 7858 $
 */
package gov.redhawk.plotter.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class PlotterApplication implements IApplication {

	public Object start(final IApplicationContext context) throws Exception {
		final Display display = PlatformUI.createDisplay();
		//TODO RAP RCP runtime has SWT.OpenDocument
		display.addListener(SWT.OPEN, new Listener() {

			public void handleEvent(final Event event) {
				// BEGIN DEBUG CODE
				System.out.println("Open document event");
				// END DEBUG CODE
			}
		});

		try {
			final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			} else {
				return IApplication.EXIT_OK;
			}
		} finally {
			display.dispose();
		}
	}

	public void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}

}
