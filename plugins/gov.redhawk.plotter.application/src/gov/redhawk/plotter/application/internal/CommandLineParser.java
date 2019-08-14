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
package gov.redhawk.plotter.application.internal;

import gov.redhawk.plotter.application.PlotterApplicationPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Parses command line args to the plotter application.
 */
public class CommandLineParser {
	
	private String ior = null;
	private String repId = null;
	private String name = "data";
	private String connectionId = null;
	
	public String getIor() {
		return ior;
	}
	
	public String getRepId() {
		return repId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getConnectionId() {
		return connectionId;
	}
	
	/**
	 * Parses the command-line arguments.
	 * @param args The command-line arguments
	 * @return True on success, false on failure
	 */
	public boolean parse(String[] args) {
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];

			if ("-ior".equalsIgnoreCase(arg)) {
				if (i < args.length - 1) {
					ior = args[++i];
				} else {
					showUsage("Missing argument to -ior");
					return false;
				}
			} else if ("-repid".equalsIgnoreCase(arg)) {
				if (i < args.length - 1) {
					repId = args[++i];
				} else {
					showUsage("Missing argument to -repid");
					return false;
				}
			} else if ("-portname".equalsIgnoreCase(arg)) {
				if (i < args.length - 1) {
					name = args[++i];
				} else {
					showUsage("Missing argument to -portname");
					return false;
				}
			} else if ("-connectionID".equalsIgnoreCase(arg)) {
				if (i < args.length - 1) {
					connectionId = args[++i];
				} else {
					showUsage("Missing argument to -connectionID");
					return false;
				}
			} else if ("-clearPersistedState".equals(arg)) {
				// PASS - This isn't consumed by Eclipse for some reason
			} else {
				postLog("WARNING: Ignoring argument " + arg, null);
			}
		}

		if ((ior == null) || (repId == null)) {
			showUsage("Missing one or more required arguments");
			return false;
		}
		
		return true;
	}
	
	private void postLog(String log, Throwable e) {
		System.err.println(log); // SUPPRESS CHECKSTYLE ERROR LOG
		PlotterApplicationPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PlotterApplicationPlugin.PLUGIN_ID, log, e));
	}

	private void showUsage(String errorMessage) {
		if (errorMessage != null) {
			postLog(errorMessage, null);
		}

		URL usageURL = PlotterApplicationPlugin.getDefault().getBundle().getEntry("usage.txt");
		try (FileInputStream fis = new FileInputStream(new File(FileLocator.resolve(usageURL).toURI()))) {
			try (BufferedReader input = new BufferedReader(new InputStreamReader(fis))) {
				String line = input.readLine();
				while (line != null) {
					System.err.println(line); // SUPPRESS CHECKSTYLE ERROR LOG
					line = input.readLine();
				}
				input.close();
			} catch (IOException ex) {
				PlotterApplicationPlugin.getDefault().getLog().log(
					new Status(IStatus.ERROR, PlotterApplicationPlugin.PLUGIN_ID, "Unable to display error message", ex));
			}
		} catch (URISyntaxException ex) {
			PlotterApplicationPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PlotterApplicationPlugin.PLUGIN_ID, "Error loading usage text", ex));
			return;
		} catch (IOException ex) {
			PlotterApplicationPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PlotterApplicationPlugin.PLUGIN_ID, "Error loading usage text", ex));
			return;
		}
	}
}
