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
package gov.redhawk.plotter.application;

import gov.redhawk.model.sca.ScaFactory;
import gov.redhawk.model.sca.ScaUsesPort;
import gov.redhawk.sca.util.ORBUtil;
import gov.redhawk.ui.port.nxmplot.PlotType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.jpeojtrs.sca.scd.ScdFactory;
import mil.jpeojtrs.sca.scd.Uses;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.commands.ICommandService;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public void preWindowOpen() {
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1024, 768)); // SUPPRESS CHECKSTYLE MagicNumber
		configurer.setShowMenuBar(false);
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setShowFastViewBars(true);
		configurer.setShowProgressIndicator(true);
		configurer.setTitle("REDHAWK Plotter");
	}

	@Override
	public void postWindowOpen() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (!PlotterPerspective.ID.equals(window.getActivePage().getPerspective().getId())) {
			try {
				PlatformUI.getWorkbench().showPerspective(PlotterPerspective.ID, window);
				window.getActivePage().resetPerspective();
			} catch (final WorkbenchException e) {
				PlotterApplicationPlugin.getDefault()
				        .getLog()
				        .log(new Status(IStatus.ERROR, PlotterApplicationPlugin.PLUGIN_ID, "Failed to open default Plotter Perspective: "
				                + PlotterPerspective.ID));
			}
		} else {
			window.getActivePage().resetPerspective();
		}

		// Arguments from command line
		String ior = null;
		String repId = null;
		String handlerid = null;
		String name = null;

		final String[] args = Platform.getApplicationArgs();
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];

			if ("-ior".equals(arg)) {
				if (i < args.length) {
					ior = args[++i];
				}
			} else if ("-repid".equals(arg)) {
				if (i < args.length) {
					repId = args[++i];
				}
			} else if ("-handler".equals(arg)) {
				if (i < args.length) {
					handlerid = args[++i];
				}
			} else if ("-portname".equals(arg)) {
				if (i < args.length) {
					name = args[++i];
				}
			}
		}

		if ((ior != null) && (repId != null) && (handlerid != null) && (name != null)) {
			// Create the ORB
			final org.omg.CORBA.ORB orb = ORBUtil.init(System.getProperties());
			final org.omg.CORBA.Object obj = orb.string_to_object(ior);

			gov.redhawk.ui.port.Activator.getDefault();
			// Locate the handler
			
//			final IPortHandlerRegistry handlerRegistry = Activator.getPortHandlerRegistry();
//			final IPortHandler handler = handlerRegistry.findPortHandler(handlerid, repId);
//			if (handler == null) {
//				PlotterApplicationPlugin.getDefault()
//				        .getLog()
//				        .log(new Status(IStatus.ERROR,
//				                PlotterApplicationPlugin.PLUGIN_ID,
//				                "Could not find associated  plot handler, check -handler and -repid arguments."));
//				PlatformUI.getWorkbench().close();
//				return; // Will never get here
//			}

			// Create the port objects
			final Uses profile = ScdFactory.eINSTANCE.createUses();
			profile.setRepID(repId);
			profile.setName(name);

			final ScaUsesPort port = ScaFactory.eINSTANCE.createScaUsesPort();
			port.setCorbaObj(obj);
			port.setProfileObj(profile);

			// Connect the port
			final List<ScaUsesPort> ports = new ArrayList<ScaUsesPort>();
			ports.add(port);
			EvaluationContext exContext = new EvaluationContext(null, ports);
			exContext.addVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME, new StructuredSelection(ports));
			exContext.addVariable(ISources.ACTIVE_WORKBENCH_WINDOW_NAME, window);
			Map<String, Object> exParam = new HashMap<String, Object>();
			exParam.put("gov.redhawk.ui.port.nxmplot.type", PlotType.RASTER.toString());
			exParam.put("gov.redhawk.ui.port.nxmplot.isFft", Boolean.FALSE.toString());
			ICommandService svc = (ICommandService) window.getService(ICommandService.class);
			Command comm = svc.getCommand("gov.redhawk.ui.port.nxmplot.command.plot");
			ExecutionEvent ex = new ExecutionEvent(comm, exParam, null, exContext);
			try {
				comm.executeWithChecks(ex);
			} catch (ExecutionException e2) {
				postLog("Failed to execute plot handler command", e2);
			} catch (NotDefinedException e2) {
				postLog("Failed to execute plot handler command", e2);
			} catch (NotEnabledException e2) {
				postLog("Failed to execute plot handler command", e2);
			} catch (NotHandledException e2) {
				postLog("Failed to execute plot handler command", e2);
				
			}
		} else {
			PlotterApplicationPlugin.getDefault()
			        .getLog()
			        .log(new Status(IStatus.ERROR,
			                PlotterApplicationPlugin.PLUGIN_ID,
			                "Missing one or more required arguments: -ior, -repid, -handler, -portname"));
			PlatformUI.getWorkbench().close();
		}

	}

	private void postLog(String log, Throwable e) {
		PlotterApplicationPlugin.getDefault()
        .getLog()
        .log(new Status(IStatus.ERROR,
                PlotterApplicationPlugin.PLUGIN_ID,
                log, e));
		
	}

}