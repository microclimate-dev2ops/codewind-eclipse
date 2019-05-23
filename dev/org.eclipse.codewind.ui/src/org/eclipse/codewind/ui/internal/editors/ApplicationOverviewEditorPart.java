/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.ui.internal.editors;

import org.eclipse.codewind.core.internal.CodewindApplication;
import org.eclipse.codewind.ui.CodewindUIPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

public class ApplicationOverviewEditorPart extends EditorPart {
	
	private CodewindApplication app;
	private Composite contents;
	private boolean isDirty = false;

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
        setInput(input);
        
        // Get the app from the editor input
        if (input instanceof ApplicationOverviewEditorInput) {
        	app = ((ApplicationOverviewEditorInput)input).app;
        } else {
        	app = null;
        }
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		ManagedForm managedForm = new ManagedForm(parent);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		form.setText("Overview: " + app.name);
		form.setImage(CodewindUIPlugin.getImage(CodewindUIPlugin.MICROCLIMATE_ICON));
		form.getBody().setLayout(new GridLayout());
		
		Composite columnComp = toolkit.createComposite(form.getBody());
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 10;
		columnComp.setLayout(layout);
		columnComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
		
		// left column
		Composite leftColumnComp = toolkit.createComposite(columnComp);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 0;
		leftColumnComp.setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		data.widthHint = 120;
		leftColumnComp.setLayoutData(data);
		
		createGeneralSection(leftColumnComp, toolkit);
		
		// right column
		Composite rightColumnComp = toolkit.createComposite(columnComp);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 10;
		layout.horizontalSpacing = 0;
		rightColumnComp.setLayout(layout);
		rightColumnComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
		
		createDebugSection(rightColumnComp, toolkit);

		form.reflow(true);
	}
	
	private void createGeneralSection(Composite parent, FormToolkit toolkit) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
        section.setText("General");
        section.setDescription("General information and settings");
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));

        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 10;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
        toolkit.paintBordersFor(composite);
        section.setClient(composite);
        
        addStringEntry(composite, "Language", app.projectType.language, true);
        addStringEntry(composite, "Location", app.fullLocalPath.toOSString(), true);
        addBooleanEntry(composite, "Status", null, "Enabled", "Disabled", app.isActive(), true);
        addStringEntry(composite, "Application URL", app.getBaseUrl() != null ? app.getBaseUrl().toString() : null, app.isActive());
        addTextEntry(composite, "Application port", app.getHttpPort() > 0 ? Integer.toString(app.getHttpPort()) : null, app.isActive());
	}
	
	private void createDebugSection(Composite parent, FormToolkit toolkit) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
        section.setText("Debug");
        section.setDescription("Debug information and settings");
        section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
        section.setExpanded(true);

        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 10;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
        toolkit.paintBordersFor(composite);
        section.setClient(composite);
        
        addBooleanEntry(composite, "Enable debug mode", CodewindUIPlugin.getImage(CodewindUIPlugin.LAUNCH_DEBUG_ICON), "On", "Off", false, app.isActive());
        addStringEntry(composite, "Debug port", app.getDebugPort() > 0 ? Integer.toString(app.getDebugPort()) : null, app.isActive());
	}
	
	private void addStringEntry(Composite composite, String name, String value, boolean enabled) {
		StyledText label = new StyledText(composite, SWT.NONE);
		label.setText(name);
        setBold(label);
        
        Text text = new Text(composite, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
        text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        text.setText(value != null ? value : "Not available");
        text.setEnabled(false);
        
        new Label(composite, SWT.NONE);
	}
	
	private void addTextEntry(Composite composite, String name, String value, boolean enabled) {
		StyledText label = new StyledText(composite, SWT.NONE);
		label.setText(name);
        setBold(label);
        
        Text text = new Text(composite, SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        text.setText(value != null ? value : "Not available");
        text.setEnabled(enabled);
        
        new Label(composite, SWT.NONE);
	}
	
	private void addBooleanEntry(Composite composite, String name, Image image, String onText, String offText, boolean value, boolean enabled) {
		StyledText label = new StyledText(composite, SWT.NONE);
		label.setText(name);
        setBold(label);
        
        Button button = new Button(composite, SWT.TOGGLE);
        button.setSelection(value);
        if (image != null) {
        	button.setImage(image);
        }
        button.setText(button.getSelection() ? onText : offText);
        button.setEnabled(enabled);
        button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				button.setText(button.getSelection() ? onText : offText);
				isDirty = true;
			}
		});
        
        new Label(composite, SWT.NONE);
	}
	
	private void setBold(StyledText text) {
		StyleRange range = new StyleRange();
        range.start = 0;
        range.length = text.getText().length();
        range.fontStyle = SWT.BOLD;
        text.setStyleRange(range);
	}

	@Override
	public void setFocus() {
		if (contents != null) {
			contents.setFocus();
		}
	}

}
