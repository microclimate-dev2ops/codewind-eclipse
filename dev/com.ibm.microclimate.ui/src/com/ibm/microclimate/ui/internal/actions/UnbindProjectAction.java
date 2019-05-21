/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *	 IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.ibm.microclimate.ui.internal.actions;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.actions.SelectionProviderAction;

import com.ibm.microclimate.core.internal.MCEclipseApplication;
import com.ibm.microclimate.core.internal.MCLogger;
import com.ibm.microclimate.core.internal.MCUtil;
import com.ibm.microclimate.ui.internal.messages.Messages;

/**
 * Action for unbinding a Codewind project.
 */
public class UnbindProjectAction extends SelectionProviderAction {
	
	MCEclipseApplication app;
	
	public UnbindProjectAction(ISelectionProvider selectionProvider) {
		super(selectionProvider, Messages.UnbindActionLabel);
		selectionChanged(getStructuredSelection());
	}

	@Override
	public void selectionChanged(IStructuredSelection sel) {
		if (sel.size() == 1) {
			Object obj = sel.getFirstElement();
			if (obj instanceof MCEclipseApplication) {
				app = (MCEclipseApplication) obj;
				setEnabled(true);
				return;
			}
		}
		setEnabled(false);
	}

	@Override
	public void run() {
		if (app == null) {
			// should not be possible
			MCLogger.logError("UnbindProjectAction ran but no application was selected"); //$NON-NLS-1$
			return;
		}
		
		try {
			app.mcConnection.requestProjectUnbind(app.projectID);
		} catch (Exception e) {
			MCLogger.logError("Error requesting application remove: " + app.name, e); //$NON-NLS-1$
			MCUtil.openDialog(true, NLS.bind(Messages.UnbindActionError, app.name), e.getMessage());
			return;
		}
	}
}
