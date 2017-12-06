package com.mdm.interfaces.endpoints;

import com.mdm.application.dto.ChangeServiceStateDto;
import com.mdm.application.dto.DeviceDtoInput;
import com.mdm.application.dto.SendMessageDto;
import com.mdm.interfaces.endpoints.responses.MDMResultWebServiceCall;

public interface MDMController {
	
	public String[][] getAllDevices();
	
	public MDMResultWebServiceCall connectDeviseToMDM( DeviceDtoInput device );
	
	public MDMResultWebServiceCall sendMessage(SendMessageDto dto);
	
	public MDMResultWebServiceCall sendMessageToAllDevices(SendMessageDto dto);
	
	public MDMResultWebServiceCall rebootAllDevices();
	
	public MDMResultWebServiceCall destroyAllDevices();
	
	public MDMResultWebServiceCall changeServiceState( ChangeServiceStateDto dto );
	
	public MDMResultWebServiceCall sendApk( String apkPath );
	
	public String[] getStoredApksName();

}
