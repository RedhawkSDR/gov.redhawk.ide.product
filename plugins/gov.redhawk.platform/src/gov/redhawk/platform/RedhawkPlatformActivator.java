package gov.redhawk.platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogManager;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RedhawkPlatformActivator extends AbstractUIPlugin implements
		IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "gov.redhawk.platform"; //$NON-NLS-1$

	// The shared instance
	private static RedhawkPlatformActivator plugin;

	/**
	 * The constructor
	 */
	public RedhawkPlatformActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		configureJavaLogger(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static RedhawkPlatformActivator getDefault() {
		return plugin;
	}

	@Override
	public void earlyStartup() {
		// PASS - logic is in startup
	}

	/**
	 * This method ensures that if the Java logging properties weren't properly
	 * installed by the IDE's feature into the configuration directory that
	 * we'll load a backup. This is primarily important for debugging the IDE
	 * within Eclipse where this is the situation.
	 * 
	 * @param context
	 */
	private void configureJavaLogger(final BundleContext context) {
		InputStream test = null;
		InputStream logInputStream = null;
		try {
			test = Platform.getConfigurationLocation()
					.getDataArea("javalogger.properties").openStream();
		} catch (IOException e) {
			URL javaloggerURL = FileLocator.find(context.getBundle(), new Path(
					"javalogger.properties"), null);
			if (javaloggerURL != null) {
				try {
					logInputStream = javaloggerURL.openStream();
					LogManager.getLogManager()
							.readConfiguration(logInputStream);
				} catch (IOException e2) {
					// PASS
				} catch (SecurityException e2) {
					// PASS
				}
			}
		} finally {
			if (test != null) {
				try {
					test.close();
				} catch (IOException e) {
					// PASS
				}
			}
			if (logInputStream != null) {
				try {
					logInputStream.close();
				} catch (IOException e) {
					// PASS
				}
			}
		}
	}

}
