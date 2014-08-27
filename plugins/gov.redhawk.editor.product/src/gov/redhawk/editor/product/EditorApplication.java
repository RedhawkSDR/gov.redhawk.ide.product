package gov.redhawk.editor.product;

import gov.redhawk.editor.product.internal.EditorApplicationWorkbenchAdvisor;

import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEApplication;


public class EditorApplication extends IDEApplication implements IApplication, IExecutableExtension {



    /* (non-Javadoc)
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext context)
     */
    public Object start(IApplicationContext appContext) throws Exception {
        Display display = createDisplay();
        // processor must be created before we start event loop
        DelayedEventsProcessor processor = new DelayedEventsProcessor(display);

        try {
        	int returnCode = PlatformUI.createAndRunWorkbench(display, new EditorApplicationWorkbenchAdvisor(processor));
    		if (returnCode == PlatformUI.RETURN_RESTART) {
    			return IApplication.EXIT_RESTART;
    		}

    		return IApplication.EXIT_OK;
        } finally {
            if (display != null) {
				display.dispose();
			}
            Location instanceLoc = Platform.getInstanceLocation();
            if (instanceLoc != null)
            	instanceLoc.release();
        }
    }
}
