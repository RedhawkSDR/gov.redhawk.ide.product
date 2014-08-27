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

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.ide.IIDEActionConstants;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.handlers.IActionCommandMappingService;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.provisional.application.IActionBarConfigurer2;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IMenuService;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class EditorApplicationActionBarAdvisor extends ActionBarAdvisor {
	private final IWorkbenchWindow window;

	// generic actions
	private IWorkbenchAction closeAction;

	private IWorkbenchAction closeAllAction;

	private IWorkbenchAction closeOthersAction;

	private IWorkbenchAction closeAllSavedAction;

	private IWorkbenchAction saveAction;

	private IWorkbenchAction saveAllAction;

	private IWorkbenchAction helpContentsAction;

	private IWorkbenchAction helpSearchAction;

	private IWorkbenchAction dynamicHelpAction;

	private IWorkbenchAction aboutAction;

	private IWorkbenchAction saveAsAction;

	private IWorkbenchAction hideShowEditorAction;

	private IWorkbenchAction editActionSetAction;

	private IWorkbenchAction lockToolBarAction;

	private IWorkbenchAction nextPartAction;

	private IWorkbenchAction prevPartAction;

	private IWorkbenchAction nextEditorAction;

	private IWorkbenchAction prevEditorAction;

	private IWorkbenchAction activateEditorAction;

	private IWorkbenchAction maximizePartAction;

	private IWorkbenchAction minimizePartAction;

	private IWorkbenchAction switchToEditorAction;

	private IWorkbenchAction workbookEditorsAction;

	private IWorkbenchAction quickAccessAction;

	private IWorkbenchAction backwardHistoryAction;

	private IWorkbenchAction forwardHistoryAction;

	// generic retarget actions
	private IWorkbenchAction undoAction;

	private IWorkbenchAction redoAction;

	private IWorkbenchAction quitAction;

	private IWorkbenchAction goIntoAction;

	private IWorkbenchAction backAction;

	private IWorkbenchAction forwardAction;

	private IWorkbenchAction upAction;

	private IWorkbenchAction nextAction;

	private IWorkbenchAction previousAction;

	/**
	 * Indicates if the action builder has been disposed
	 */
	private boolean isDisposed = false;

	/**
	 * The coolbar context menu manager.
	 * @since 3.3
	 */
	private MenuManager coolbarPopupMenuManager;

	/**
	 * Constructs a new action builder which contributes actions
	 * to the given window.
	 * 
	 * @param configurer the action bar configurer for the window
	 */
	public EditorApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		window = configurer.getWindowConfigurer().getWindow();
	}

	/**
	 * Returns the window to which this action builder is contributing.
	 */
	private IWorkbenchWindow getWindow() {
		return window;
	}

	/**
	 * Fills the coolbar with the workbench actions.
	 */
	protected void fillCoolBar(ICoolBarManager coolBar) {

		IActionBarConfigurer2 actionBarConfigurer = (IActionBarConfigurer2) getActionBarConfigurer();
		{ // Set up the context Menu
			coolbarPopupMenuManager = new MenuManager();
			coolbarPopupMenuManager.add(new ActionContributionItem(lockToolBarAction));
			coolbarPopupMenuManager.add(new ActionContributionItem(editActionSetAction));
			coolBar.setContextMenuManager(coolbarPopupMenuManager);
			IMenuService menuService = (IMenuService) window.getService(IMenuService.class);
			menuService.populateContributionManager(coolbarPopupMenuManager, "popup:windowCoolbarContextMenu"); //$NON-NLS-1$
		}
		coolBar.add(new GroupMarker(IIDEActionConstants.GROUP_FILE));
		{ // File Group
			IToolBarManager fileToolBar = actionBarConfigurer.createToolBarManager();
			fileToolBar.add(new GroupMarker(IWorkbenchActionConstants.SAVE_GROUP));
			fileToolBar.add(saveAction);
			fileToolBar.add(saveAllAction);
			fileToolBar.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
			fileToolBar.add(getPrintItem());
			fileToolBar.add(new GroupMarker(IWorkbenchActionConstants.PRINT_EXT));

			fileToolBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

			// Add to the cool bar manager
			coolBar.add(actionBarConfigurer.createToolBarContributionItem(fileToolBar, IWorkbenchActionConstants.TOOLBAR_FILE));
		}

		coolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		coolBar.add(new GroupMarker(IIDEActionConstants.GROUP_NAV));
		{ // Navigate group
			IToolBarManager navToolBar = actionBarConfigurer.createToolBarManager();
			navToolBar.add(new Separator(IWorkbenchActionConstants.HISTORY_GROUP));
			navToolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_APP));
			navToolBar.add(backwardHistoryAction);
			navToolBar.add(forwardHistoryAction);

			// Add to the cool bar manager
			coolBar.add(actionBarConfigurer.createToolBarContributionItem(navToolBar, IWorkbenchActionConstants.TOOLBAR_NAVIGATE));
		}

		coolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_EDITOR));

		coolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_HELP));

		{ // Help group
			IToolBarManager helpToolBar = actionBarConfigurer.createToolBarManager();
			helpToolBar.add(new Separator(IWorkbenchActionConstants.GROUP_HELP));
//            helpToolBar.add(searchComboItem);
			// Add the group for applications to contribute
			helpToolBar.add(new GroupMarker(IWorkbenchActionConstants.GROUP_APP));
			// Add to the cool bar manager
			coolBar.add(actionBarConfigurer.createToolBarContributionItem(helpToolBar, IWorkbenchActionConstants.TOOLBAR_HELP));
		}

	}

	/**
	 * Fills the menu bar with the workbench actions.
	 */
	protected void fillMenuBar(IMenuManager menuBar) {
		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createHelpMenu());
	}

	/**
	 * Creates and returns the File menu.
	 */
	private MenuManager createFileMenu() {
		MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_file, IWorkbenchActionConstants.M_FILE);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		menu.add(new Separator());
		menu.add(closeAction);
		menu.add(closeAllAction);
		// menu.add(closeAllSavedAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
		menu.add(new Separator());
		menu.add(saveAction);
		menu.add(saveAsAction);
		menu.add(saveAllAction);
		menu.add(new Separator());
		menu.add(getRefreshItem());

		menu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
		menu.add(new Separator());
		menu.add(getPrintItem());
		menu.add(new GroupMarker(IWorkbenchActionConstants.PRINT_EXT));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		menu.add(ContributionItemFactory.REOPEN_EDITORS.create(getWindow()));
		menu.add(new GroupMarker(IWorkbenchActionConstants.MRU));

		// If we're on OS X we shouldn't show this command in the File menu. It
		// should be invisible to the user. However, we should not remove it -
		// the carbon UI code will do a search through our menu structure
		// looking for it when Cmd-Q is invoked (or Quit is chosen from the
		// application menu.
		ActionContributionItem quitItem = new ActionContributionItem(quitAction);
		menu.add(quitItem);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return menu;
	}

	/**
	 * Creates and returns the Edit menu.
	 */
	private MenuManager createEditMenu() {
		MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_edit, IWorkbenchActionConstants.M_EDIT);
		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));

		menu.add(undoAction);
		menu.add(redoAction);
		menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		menu.add(new Separator());

		menu.add(getCutItem());
		menu.add(getCopyItem());
		menu.add(getPasteItem());
		menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		menu.add(new Separator());

		menu.add(getDeleteItem());
		menu.add(getSelectAllItem());
		menu.add(new Separator());

		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		return menu;
	}

	/**
	 * Creates and returns the Help menu.
	 */
	private MenuManager createHelpMenu() {
		MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_help, IWorkbenchActionConstants.M_HELP);
		addSeparatorOrGroupMarker(menu, "group.intro"); //$NON-NLS-1$

		menu.add(new GroupMarker("group.intro.ext")); //$NON-NLS-1$
		addSeparatorOrGroupMarker(menu, "group.main"); //$NON-NLS-1$
		menu.add(helpContentsAction);
		menu.add(helpSearchAction);
		menu.add(dynamicHelpAction);

		// HELP_START should really be the first item, but it was after
		// quickStartAction and tipsAndTricksAction in 2.1.
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
		menu.add(new GroupMarker("group.main.ext")); //$NON-NLS-1$
		addSeparatorOrGroupMarker(menu, "group.tutorials"); //$NON-NLS-1$
		addSeparatorOrGroupMarker(menu, "group.tools"); //$NON-NLS-1$
		addSeparatorOrGroupMarker(menu, "group.updates"); //$NON-NLS-1$
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
		addSeparatorOrGroupMarker(menu, IWorkbenchActionConstants.MB_ADDITIONS);
		// about should always be at the bottom
		menu.add(new Separator("group.about")); //$NON-NLS-1$

		ActionContributionItem aboutItem = new ActionContributionItem(aboutAction);
		menu.add(aboutItem);
		menu.add(new GroupMarker("group.about.ext")); //$NON-NLS-1$
		return menu;
	}

	/**
	 * Adds a <code>GroupMarker</code> or <code>Separator</code> to a menu.
	 * The test for whether a separator should be added is done by checking for
	 * the existence of a preference matching the string
	 * useSeparator.MENUID.GROUPID that is set to <code>true</code>.
	 * 
	 * @param menu
	 * the menu to add to
	 * @param groupId
	 * the group id for the added separator or group marker
	 */
	private void addSeparatorOrGroupMarker(MenuManager menu, String groupId) {
		String prefId = "useSeparator." + menu.getId() + "." + groupId; //$NON-NLS-1$ //$NON-NLS-2$
		boolean addExtraSeparators = IDEWorkbenchPlugin.getDefault().getPreferenceStore().getBoolean(prefId);
		if (addExtraSeparators) {
			menu.add(new Separator(groupId));
		} else {
			menu.add(new GroupMarker(groupId));
		}
	}

	/**
	 * Disposes any resources and unhooks any listeners that are no longer needed.
	 * Called when the window is closed.
	 */
	public void dispose() {
		if (isDisposed) {
			return;
		}
		isDisposed = true;
		IMenuService menuService = (IMenuService) window.getService(IMenuService.class);
		menuService.releaseContributions(coolbarPopupMenuManager);
		coolbarPopupMenuManager.dispose();

		// null out actions to make leak debugging easier
		closeAction = null;
		closeAllAction = null;
		closeAllSavedAction = null;
		closeOthersAction = null;
		saveAction = null;
		saveAllAction = null;
		helpContentsAction = null;
		helpSearchAction = null;
		dynamicHelpAction = null;
		aboutAction = null;
		saveAsAction = null;
		hideShowEditorAction = null;
		editActionSetAction = null;
		lockToolBarAction = null;
		nextPartAction = null;
		prevPartAction = null;
		nextEditorAction = null;
		prevEditorAction = null;
		activateEditorAction = null;
		maximizePartAction = null;
		minimizePartAction = null;
		switchToEditorAction = null;
		quickAccessAction.dispose();
		quickAccessAction = null;
		backwardHistoryAction = null;
		forwardHistoryAction = null;
		undoAction = null;
		redoAction = null;
		quitAction = null;
		goIntoAction = null;
		backAction = null;
		forwardAction = null;
		upAction = null;
		nextAction = null;
		previousAction = null;
		super.dispose();
	}

	/**
	 * Returns true if the menu with the given ID should
	 * be considered as an OLE container menu. Container menus
	 * are preserved in OLE menu merging.
	 */
	public boolean isApplicationMenu(String menuId) {
		if (menuId.equals(IWorkbenchActionConstants.M_FILE)) {
			return true;
		}
		if (menuId.equals(IWorkbenchActionConstants.M_WINDOW)) {
			return true;
		}
		return false;
	}

	/**
	 * Return whether or not given id matches the id of the coolitems that
	 * the workbench creates.
	 */
	public boolean isWorkbenchCoolItemId(String id) {
		if (IWorkbenchActionConstants.TOOLBAR_FILE.equalsIgnoreCase(id)) {
			return true;
		}
		if (IWorkbenchActionConstants.TOOLBAR_NAVIGATE.equalsIgnoreCase(id)) {
			return true;
		}
		return false;
	}

	/**
	 * Creates actions (and contribution items) for the menu bar, toolbar and status line.
	 */
	protected void makeActions(final IWorkbenchWindow window) {
		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);

		saveAsAction = ActionFactory.SAVE_AS.create(window);
		register(saveAsAction);

		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAllAction);

		undoAction = ActionFactory.UNDO.create(window);
		register(undoAction);

		redoAction = ActionFactory.REDO.create(window);
		register(redoAction);

		closeAction = ActionFactory.CLOSE.create(window);
		register(closeAction);

		closeAllAction = ActionFactory.CLOSE_ALL.create(window);
		register(closeAllAction);

		closeOthersAction = ActionFactory.CLOSE_OTHERS.create(window);
		register(closeOthersAction);

		closeAllSavedAction = ActionFactory.CLOSE_ALL_SAVED.create(window);
		register(closeAllSavedAction);

		helpContentsAction = ActionFactory.HELP_CONTENTS.create(window);
		register(helpContentsAction);

		helpSearchAction = ActionFactory.HELP_SEARCH.create(window);
		register(helpSearchAction);

		dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
		register(dynamicHelpAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		aboutAction.setImageDescriptor(IDEInternalWorkbenchImages.getImageDescriptor(IDEInternalWorkbenchImages.IMG_OBJS_DEFAULT_PROD));
		register(aboutAction);
		
		nextEditorAction = ActionFactory.NEXT_EDITOR.create(window);
		register(nextEditorAction);
		prevEditorAction = ActionFactory.PREVIOUS_EDITOR.create(window);
		register(prevEditorAction);
		ActionFactory.linkCycleActionPair(nextEditorAction, prevEditorAction);

		nextPartAction = ActionFactory.NEXT_PART.create(window);
		register(nextPartAction);
		prevPartAction = ActionFactory.PREVIOUS_PART.create(window);
		register(prevPartAction);
		ActionFactory.linkCycleActionPair(nextPartAction, prevPartAction);

		activateEditorAction = ActionFactory.ACTIVATE_EDITOR.create(window);
		register(activateEditorAction);

		maximizePartAction = ActionFactory.MAXIMIZE.create(window);
		register(maximizePartAction);

		minimizePartAction = ActionFactory.MINIMIZE.create(window);
		register(minimizePartAction);

		switchToEditorAction = ActionFactory.SHOW_OPEN_EDITORS.create(window);
		register(switchToEditorAction);

		workbookEditorsAction = ActionFactory.SHOW_WORKBOOK_EDITORS.create(window);
		register(workbookEditorsAction);

		quickAccessAction = ActionFactory.SHOW_QUICK_ACCESS.create(window);

		hideShowEditorAction = ActionFactory.SHOW_EDITOR.create(window);
		register(hideShowEditorAction);
		editActionSetAction = ActionFactory.EDIT_ACTION_SETS.create(window);
		register(editActionSetAction);
		lockToolBarAction = ActionFactory.LOCK_TOOL_BAR.create(window);
		register(lockToolBarAction);

		forwardHistoryAction = ActionFactory.FORWARD_HISTORY.create(window);
		register(forwardHistoryAction);

		backwardHistoryAction = ActionFactory.BACKWARD_HISTORY.create(window);
		register(backwardHistoryAction);

		quitAction = ActionFactory.QUIT.create(window);
		register(quitAction);

		goIntoAction = ActionFactory.GO_INTO.create(window);
		register(goIntoAction);

		backAction = ActionFactory.BACK.create(window);
		register(backAction);

		forwardAction = ActionFactory.FORWARD.create(window);
		register(forwardAction);

		upAction = ActionFactory.UP.create(window);
		register(upAction);

		nextAction = ActionFactory.NEXT.create(window);
		nextAction.setImageDescriptor(IDEInternalWorkbenchImages.getImageDescriptor(IDEInternalWorkbenchImages.IMG_ETOOL_NEXT_NAV));
		register(nextAction);

		previousAction = ActionFactory.PREVIOUS.create(window);
		previousAction.setImageDescriptor(IDEInternalWorkbenchImages.getImageDescriptor(IDEInternalWorkbenchImages.IMG_ETOOL_PREVIOUS_NAV));
		register(previousAction);
	}

	private IContributionItem getCutItem() {
		return getItem(ActionFactory.CUT.getId(), ActionFactory.CUT.getCommandId(), ISharedImages.IMG_TOOL_CUT, ISharedImages.IMG_TOOL_CUT_DISABLED,
			WorkbenchMessages.Workbench_cut, WorkbenchMessages.Workbench_cutToolTip, null);
	}

	private IContributionItem getCopyItem() {
		return getItem(ActionFactory.COPY.getId(), ActionFactory.COPY.getCommandId(), ISharedImages.IMG_TOOL_COPY, ISharedImages.IMG_TOOL_COPY_DISABLED,
			WorkbenchMessages.Workbench_copy, WorkbenchMessages.Workbench_copyToolTip, null);
	}

	private IContributionItem getPasteItem() {
		return getItem(ActionFactory.PASTE.getId(), ActionFactory.PASTE.getCommandId(), ISharedImages.IMG_TOOL_PASTE, ISharedImages.IMG_TOOL_PASTE_DISABLED,
			WorkbenchMessages.Workbench_paste, WorkbenchMessages.Workbench_pasteToolTip, null);
	}

	private IContributionItem getPrintItem() {
		return getItem(ActionFactory.PRINT.getId(), ActionFactory.PRINT.getCommandId(), ISharedImages.IMG_ETOOL_PRINT_EDIT,
			ISharedImages.IMG_ETOOL_PRINT_EDIT_DISABLED, WorkbenchMessages.Workbench_print, WorkbenchMessages.Workbench_printToolTip, null);
	}

	private IContributionItem getSelectAllItem() {
		return getItem(ActionFactory.SELECT_ALL.getId(), ActionFactory.SELECT_ALL.getCommandId(), null, null, WorkbenchMessages.Workbench_selectAll,
			WorkbenchMessages.Workbench_selectAllToolTip, null);
	}

	private IContributionItem getDeleteItem() {
		return getItem(ActionFactory.DELETE.getId(), ActionFactory.DELETE.getCommandId(), ISharedImages.IMG_TOOL_DELETE,
			ISharedImages.IMG_TOOL_DELETE_DISABLED, WorkbenchMessages.Workbench_delete, WorkbenchMessages.Workbench_deleteToolTip,
			IWorkbenchHelpContextIds.DELETE_RETARGET_ACTION);
	}

	private IContributionItem getRevertItem() {
		return getItem(ActionFactory.REVERT.getId(), ActionFactory.REVERT.getCommandId(), null, null, WorkbenchMessages.Workbench_revert,
			WorkbenchMessages.Workbench_revertToolTip, null);
	}

	private IContributionItem getRefreshItem() {
		return getItem(ActionFactory.REFRESH.getId(), ActionFactory.REFRESH.getCommandId(), null, null, WorkbenchMessages.Workbench_refresh,
			WorkbenchMessages.Workbench_refreshToolTip, null);
	}

	private IContributionItem getRenameItem() {
		return getItem(ActionFactory.RENAME.getId(), ActionFactory.RENAME.getCommandId(), null, null, WorkbenchMessages.Workbench_rename,
			WorkbenchMessages.Workbench_renameToolTip, null);
	}

	private IContributionItem getItem(String actionId, String commandId, String image, String disabledImage, String label, String tooltip, String helpContextId) {
		ISharedImages sharedImages = getWindow().getWorkbench().getSharedImages();

		IActionCommandMappingService acms = (IActionCommandMappingService) getWindow().getService(IActionCommandMappingService.class);
		acms.map(actionId, commandId);

		CommandContributionItemParameter commandParm = new CommandContributionItemParameter(getWindow(), actionId, commandId, null,
			sharedImages.getImageDescriptor(image), sharedImages.getImageDescriptor(disabledImage), null, label, null, tooltip,
			CommandContributionItem.STYLE_PUSH, null, false);
		return new CommandContributionItem(commandParm);
	}

}
