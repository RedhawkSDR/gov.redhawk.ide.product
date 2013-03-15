/**
 * REDHAWK HEADER
 *
 * Identification: $Revision: 7858 $
 */
package gov.redhawk.plotter.application;

import gov.redhawk.ui.port.nxmplot.PlotActivator;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PlotterPerspective implements IPerspectiveFactory {
	public static final String ID = "gov.redhawk.plotter.application.perspective"; //$NON-NLS-1$

	public void createInitialLayout(final IPageLayout layout) {
		final String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		// Place project explorer to left of editor area.
		layout.addView(PlotActivator.VIEW_PLOT, IPageLayout.LEFT, (float) 1.0, editorArea);
	}

}
