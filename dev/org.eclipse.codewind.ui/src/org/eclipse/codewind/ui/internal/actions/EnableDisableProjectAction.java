/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.ui.internal.actions;

import org.eclipse.codewind.core.internal.CodewindEclipseApplication;
import org.eclipse.codewind.core.internal.Logger;
import org.eclipse.codewind.core.internal.CoreUtil;
import org.eclipse.codewind.ui.internal.messages.Messages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action for enabling/disabling a Codewind project.  Not currently used.
 */
public class EnableDisableProjectAction implements IObjectActionDelegate {

    protected CodewindEclipseApplication app;

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {
            action.setEnabled(false);
            return;
        }

        IStructuredSelection sel = (IStructuredSelection) selection;
        if (sel.size() == 1) {
            Object obj = sel.getFirstElement();
            if (obj instanceof CodewindEclipseApplication) {
            	app = (CodewindEclipseApplication)obj;
            	if (app.isAvailable()) {
                	action.setText(Messages.DisableProjectLabel);
                } else {
                	action.setText(Messages.EnableProjectLabel);
                }
	            action.setEnabled(true);
            	return;
            }
        }
        
        action.setEnabled(false);
    }

    @Override
    public void run(IAction action) {
        if (app == null) {
        	// should not be possible
        	Logger.logError("EnableDisableProjectAction ran but no application was selected"); //$NON-NLS-1$
			return;
		}

        try {
			app.connection.requestProjectOpenClose(app, !app.isEnabled());
		} catch (Exception e) {
			Logger.logError("Error initiating enable/disable for project: " + app.name, e); //$NON-NLS-1$
			CoreUtil.openDialog(true, Messages.ErrorOnEnableDisableProjectDialogTitle, e.getMessage());
			return;
		}
    }

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// nothing
	}
}
