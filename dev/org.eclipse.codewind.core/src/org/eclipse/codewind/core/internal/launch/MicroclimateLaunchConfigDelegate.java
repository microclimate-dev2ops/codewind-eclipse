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

package org.eclipse.codewind.core.internal.launch;

import java.io.IOException;

import org.eclipse.codewind.core.internal.MCLogger;
import org.eclipse.codewind.core.internal.MCUtil;
import org.eclipse.codewind.core.internal.MicroclimateApplication;
import org.eclipse.codewind.core.internal.messages.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;

@SuppressWarnings("restriction")
public class MicroclimateLaunchConfigDelegate extends AbstractJavaLaunchConfigurationDelegate {
	
	public static final String LAUNCH_CONFIG_ID = "org.eclipse.codewind.core.internal.launchConfigurationType";
	
	public static final String PROJECT_NAME_ATTR = "org.eclipse.codewind.core.internal.projectNameAttr";
	public static final String HOST_ATTR = "org.eclipse.codewind.core.internal.hostAttr";
	public static final String DEBUG_PORT_ATTR = "org.eclipse.codewind.core.internal.debugPort";
	
	@Override
	public void launch(ILaunchConfiguration config, String launchMode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		try {
			launchInner(config, launchMode, launch, monitor);
		}
		catch(CoreException e) {
			monitor.setCanceled(true);
			getLaunchManager().removeLaunch(launch);
			throw e;
		}
	}

	private void launchInner(ILaunchConfiguration config, String launchMode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		
		String projectName = config.getAttribute(PROJECT_NAME_ATTR, (String)null);
		String host = config.getAttribute(HOST_ATTR, (String)null);
		int debugPort = config.getAttribute(DEBUG_PORT_ATTR, -1);
		if (projectName == null || host == null || debugPort <= 0) {
        	String msg = "The launch configuration did not contain the required attributes: " + config.getName();		// $NON-NLS-1$
            MCLogger.logError(msg);
            abort(msg, null, IStatus.ERROR);
        }
		
		setDefaultSourceLocator(launch, config);
		
		MCLogger.log("Connecting the debugger"); //$NON-NLS-1$
		try {
			IDebugTarget debugTarget = MicroclimateDebugConnector.connectDebugger(launch, monitor);
			if (debugTarget != null) {
				MCLogger.log("Debugger connect success. Application should go into Debugging state soon."); //$NON-NLS-1$
				launch.addDebugTarget(debugTarget);
			}
			else {
				MCLogger.logError("Debugger connect failure"); //$NON-NLS-1$

				MCUtil.openDialog(true,
						Messages.MicroclimateServerBehaviour_DebuggerConnectFailureDialogTitle,
						Messages.MicroclimateServerBehaviour_DebuggerConnectFailureDialogMsg);
			}
		} catch (IllegalConnectorArgumentsException | CoreException | IOException e) {
			MCLogger.logError(e);

		}

        monitor.done();
	}
	
	public static void setConfigAttributes(ILaunchConfigurationWorkingCopy config, MicroclimateApplication app) {
		config.setAttribute(PROJECT_NAME_ATTR, app.name);
		config.setAttribute(HOST_ATTR, app.host);
		config.setAttribute(DEBUG_PORT_ATTR, app.getDebugPort());
	}
}
