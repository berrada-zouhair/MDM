package com.mdm.domain.service;

import java.util.List;

import com.mdm.application.dto.DeviceDtoInput;
import com.mdm.domain.models.actions.Action;
import com.mdm.domain.models.entities.Device;
import com.mdm.infrastructure.exception.MDMBusinessException;
import com.mdm.interfaces.notifcations.PushyAPI.PushyPushRequest;

public interface MDMSercice {

	
	public Device connectDeviceToMDM(DeviceDtoInput dto);

	public Device disconnectDeviceFromMDM(DeviceDtoInput dto) throws MDMBusinessException;

	public Device getDeviceFromMDM(String imei);

	public void receiveActionFromDevice(Action uninstallAction);
	
	public void executeActionOnDevice(Action action);

	public int send(PushyPushRequest request);
	
	public int sendToAll( Action action );

	public List<Device> getAllDevices();
	
	public List<Device> getConnectedDevices();

	public void updateDevice(Device d);
	

}
