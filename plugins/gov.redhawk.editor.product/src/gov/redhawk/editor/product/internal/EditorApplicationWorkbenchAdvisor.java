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
