package gov.redhawk.editor.product.internal;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.application.DelayedEventsProcessor;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchAdvisor;

public class EditorApplicationWorkbenchAdvisor extends IDEWorkbenchAdvisor {

	public EditorApplicationWorkbenchAdvisor() {
		super();
	}

	public EditorApplicationWorkbenchAdvisor(DelayedEventsProcessor processor) {
		super(processor);
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new EditorApplicationWorkbenchWindowAdvisor(this, configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return EditorPerspective.ID;
	}
}
