/**
 * REDHAWK HEADER
 *
 * Identification: $Revision: 7858 $
 */
package gov.redhawk.plotter.application;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class PlotterApplicationPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "gov.redhawk.plotter.application"; //$NON-NLS-1$

	// The shared instance
	private static PlotterApplicationPlugin plugin;

	/**
	 * The constructor
	 */
	public PlotterApplicationPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		PlotterApplicationPlugin.plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		PlotterApplicationPlugin.plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PlotterApplicationPlugin getDefault() {
		return PlotterApplicationPlugin.plugin;
	}

}
