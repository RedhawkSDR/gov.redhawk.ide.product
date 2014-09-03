package gov.redhawk.editor.product.tests;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotPerspective;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestEditor {

	private SWTWorkbenchBot bot;

	@Before
	public void before() {
		bot = new SWTWorkbenchBot();
	}
	
	@Test
	public void testFileMenu() {
		SWTBotPerspective perspective = bot.activePerspective();
		Assert.assertEquals("Editor", perspective.getLabel());
	}
}
