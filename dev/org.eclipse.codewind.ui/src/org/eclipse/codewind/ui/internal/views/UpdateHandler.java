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

package org.eclipse.codewind.ui.internal.views;

import org.eclipse.codewind.core.internal.IUpdateHandler;
import org.eclipse.codewind.core.internal.CodewindApplication;
import org.eclipse.codewind.core.internal.connection.CodewindConnection;

/**
 * Update handler registered on the Codewind core plug-in in order to keep
 * the Codewind view up to date.
 */
public class UpdateHandler implements IUpdateHandler {
	
	@Override
	public void updateAll() {
		ViewHelper.refreshCodewindExplorerView(null);
	}

	@Override
	public void updateConnection(CodewindConnection connection) {
		ViewHelper.refreshCodewindExplorerView(connection);
		ViewHelper.expandConnection(connection);
	}

	@Override
	public void updateApplication(CodewindApplication application) {
		ViewHelper.refreshCodewindExplorerView(application);
		ViewHelper.expandConnection(application.connection);
	}

}
