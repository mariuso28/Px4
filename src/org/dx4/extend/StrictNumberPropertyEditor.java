package org.dx4.extend;

import java.beans.PropertyEditorSupport;

public class StrictNumberPropertyEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		super.setValue(Float.parseFloat(text));
	}

	@Override
	public String getAsText() {
		return ((Number)this.getValue()).toString();
	}    
}

