/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.ui.internal.actions;

import java.util.List;

import org.eclipse.codewind.core.internal.connection.MicroclimateConnection;
import org.eclipse.codewind.core.internal.connection.MicroclimateConnectionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;

/**
 * Action provider for the Microclimate view.
 */
public class MicroclimateActionProvider extends CommonActionProvider {
	
	private CreateConnectionAction createConnectionAction;
	
    @Override
    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        Shell shell = aSite.getViewSite().getShell();
        createConnectionAction = new CreateConnectionAction(shell);
    }
    
    @Override
    public void fillContextMenu(IMenuManager menu) {
    	List<MicroclimateConnection> connections = MicroclimateConnectionManager.activeConnections();
    	if (connections == null || connections.isEmpty()) {
    		menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, createConnectionAction);
    	}
    }

}
