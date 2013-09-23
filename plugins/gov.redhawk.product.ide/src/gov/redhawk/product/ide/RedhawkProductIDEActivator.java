/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.product.ide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogManager;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * @since 1.1
 */
public class RedhawkProductIDEActivator extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "gov.redhawk.product.ide";

	// The shared instance
	private static RedhawkProductIDEActivator plugin;

	/**
	 * The constructor
	 */
	public RedhawkProductIDEActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		RedhawkProductIDEActivator.plugin = this;
		
		configureJavaLogger(context);
	}

	/**
	 * This method ensures that if the Java logging properties weren't properly installed by the IDE's feature
	 * into the configuration directory that we'll load a backup. This is primarily important for debugging the IDE
	 * within Eclipse where this is the situation.
	 * 
	 * @param context
	 */
	private void configureJavaLogger(final BundleContext context) {
	    InputStream test = null;
		InputStream logInputStream = null;
		try {
			test = Platform.getConfigurationLocation().getDataArea("javalogger.properties").openStream();
		} catch (IOException e) {
			URL javaloggerURL = FileLocator.find(context.getBundle(), new Path("javalogger.properties"), null);
			if (javaloggerURL != null) {
				try {
					logInputStream = javaloggerURL.openStream();
					LogManager.getLogManager().readConfiguration(logInputStream);
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

	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		RedhawkProductIDEActivator.plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static RedhawkProductIDEActivator getDefault() {
		return RedhawkProductIDEActivator.plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(RedhawkProductIDEActivator.PLUGIN_ID, path);
	}

	@Override
	public void earlyStartup() {
		// PASS - logic is in startup
    }
}
