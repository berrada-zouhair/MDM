package com.mdm.domain.models.actions;

import java.util.Arrays;
import java.util.List;

/** Enum representing all actions that can be performed by the MDM */
public enum ACTION_NAME {
	
	UNINSTALL_APP(PropertiesNameConst.APP_NAME),
	REBOOT_DEVICE,
	DESTROY_DEVICE,
	ENABLE_SERVICE(PropertiesNameConst.SERVICE_NAME),
	DISABLE_SERVICE(PropertiesNameConst.SERVICE_NAME),
	CHANGE_SERVICE_STATE(PropertiesNameConst.SERVICE_NAME),
	INSTALL_APP(PropertiesNameConst.APP_PATH),
	SEND_MESSAGE(PropertiesNameConst.MESSAGE_TEXT, PropertiesNameConst.BLOCK_DEVICE),
	RECEIVE_LOGS(PropertiesNameConst.LOGS_DATE),
	COONECT_DEVICE(PropertiesNameConst.COONECT_DEVICE_MESSAGE),
	RECEIVE_FILE(PropertiesNameConst.FILE_NAME),
	ACKNOWLEDGE(PropertiesNameConst.AKNOWLEDGE_VALUE);
	
	private final String[] propertiesEntries;
	
	private ACTION_NAME(String... entries) {
		this.propertiesEntries = entries;
	}

	public Object hasProperties() {
		return propertiesEntries!=null && propertiesEntries.length > 0;
	}
	
	public List<String> getPropertiesEntries() {
		return Arrays.asList(propertiesEntries);
	}

	public boolean hasPropertiy(String propertyName) {
		for (String entry : propertiesEntries) {
			if(entry.equals(propertyName))
				return true;
		}
		return false;
	}
}
