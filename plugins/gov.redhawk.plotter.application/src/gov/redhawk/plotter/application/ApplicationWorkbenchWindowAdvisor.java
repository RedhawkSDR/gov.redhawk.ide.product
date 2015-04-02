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
import gov.redhawk.plotter.application.internal.CommandLineParser;
import gov.redhawk.plotter.application.internal.TestInput;
import gov.redhawk.sca.util.OrbSession;
import gov.redhawk.ui.port.nxmplot.IPlotView;
import gov.redhawk.ui.port.nxmplot.PlotActivator;
import gov.redhawk.ui.port.nxmplot.PlotType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.jpeojtrs.sca.scd.ScdFactory;
import mil.jpeojtrs.sca.scd.Uses;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
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
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import CF.PortPOATie;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final Point INITIAL_WINDOW_SIZE = new Point(1024, 768);
	private CommandLineParser commandLine;

	public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer, CommandLineParser commandLine) {
		super(configurer);
		this.commandLine = commandLine;
	}

	@Override
	public void preWindowOpen() {
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(INITIAL_WINDOW_SIZE);
		configurer.setShowMenuBar(false);
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setShowFastViewBars(true);
		configurer.setShowProgressIndicator(true);
		configurer.setShowPerspectiveBar(false);
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
				String errorMsg = "Failed to open default Plotter Perspective: " + PlotterPerspective.ID;
				PlotterApplicationPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, PlotterApplicationPlugin.PLUGIN_ID, errorMsg, e));
			}
		} else {
			window.getActivePage().resetPerspective();
		}

		// TODO: Stuff goes here?

		// Create the ORB
		OrbSession session = OrbSession.createSession();
		final org.omg.CORBA.ORB orb = session.getOrb();
		final org.omg.CORBA.Object obj;
		if ("TEST".equals(commandLine.getIor())) {
			POA poa;
			org.omg.CORBA.Object objTmp = null;
			try {
				poa = session.getPOA();
				objTmp = poa.servant_to_reference(new PortPOATie(TestInput.INSTANCE));
				Thread thread = new Thread(TestInput.INSTANCE, "Data Thread");
				thread.setDaemon(true);
				thread.setPriority(Thread.MIN_PRIORITY);
				thread.start();
			} catch (CoreException e) {
				// PASS
			} catch (ServantNotActive e) {
				// PASS
			} catch (WrongPolicy e) {
				// PASS
			}
			obj = objTmp;
		} else {
			obj = orb.string_to_object(commandLine.getIor());
		}

		gov.redhawk.ui.port.Activator.getDefault();

		// Create the port objects
		final Uses profile = ScdFactory.eINSTANCE.createUses();
		profile.setRepID(commandLine.getRepId());
		profile.setName(commandLine.getName());

		final ScaUsesPort port = ScaFactory.eINSTANCE.createScaUsesPort();
		port.setCorbaObj(obj);
		port.setProfileObj(profile);
		port.setName(commandLine.getName());

		// Connect the port
		final List<ScaUsesPort> ports = new ArrayList<ScaUsesPort>();
		if (obj != null) {
			ports.add(port);
		}
		EvaluationContext exContext = new EvaluationContext(null, ports);
		exContext.addVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME, new StructuredSelection(ports));
		exContext.addVariable(ISources.ACTIVE_WORKBENCH_WINDOW_NAME, window);
		Map<String, Object> exParam = new HashMap<String, Object>();
		exParam.put(IPlotView.PARAM_PLOT_TYPE, PlotType.RASTER.toString());
		exParam.put(IPlotView.PARAM_ISFFT, Boolean.FALSE.toString());
		if (commandLine.getConnectionId() != null) {
			exParam.put(IPlotView.PARAM_CONNECTION_ID, commandLine.getConnectionId());
		}

		ICommandService svc = (ICommandService) window.getService(ICommandService.class);
		Command comm = svc.getCommand(IPlotView.COMMAND_ID);
		ExecutionEvent ex = new ExecutionEvent(comm, exParam, null, exContext);
		PlotActivator.getDefault().showPlotView(ex);
	}
}
