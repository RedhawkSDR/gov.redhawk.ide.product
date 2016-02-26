/**
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.plotter.application.tests;

import gov.redhawk.internal.ui.port.nxmplot.view.PlotView2;
import gov.redhawk.plotter.application.internal.TestInput;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotPerspective;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestPlotter {

	private SWTWorkbenchBot bot;

	@Before
	public void before() {
		bot = new SWTWorkbenchBot();
	}

	@SuppressWarnings("restriction")
	@Test
	public void testPlotterApp() {
		SWTBotPerspective perspective = bot.activePerspective();
		Assert.assertEquals("REDHAWK Plotter Perspective", perspective.getLabel());
		SWTBotView view = bot.activeView();
		Assert.assertEquals(PlotView2.ID, view.getViewReference().getId());

		SWTBotToolbarButton button = view.toolbarButton("Change the plot's type");
		button.click();
		bot.sleep(2000);

		button.click();
		bot.sleep(2000);

		SWTBotToolbarDropDownButton dropDown = view.toolbarDropDownButton("Change the plot's mode (Imaginary, 10 Log, etc.)");
		dropDown.menuItem("Real vs Imaginary").click();
		bot.sleep(2000);
	}

	@Test
	public void testConnectionID() {
		bot.waitUntil(new DefaultCondition() {
			@Override
			public boolean test() throws Exception {
				return "TEST_CONNECTION".equals(TestInput.INSTANCE.getConnectionID());
			}

			@Override
			public String getFailureMessage() {
				return "Connection ID never changed";
			}
		}, 60000, 1000);
	}
}
