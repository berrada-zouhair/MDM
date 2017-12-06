package com.mdm.infrastructure.utils;

import java.util.ArrayList;
import java.util.List;

import com.mdm.application.dto.DeviceDtoOutput;
import com.mdm.domain.models.entities.Device;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MDMUtils {
	
	public static DeviceDtoOutput deviceToDTO( Device device ) {
		return new DeviceDtoOutput(device.getIp(), device.getImei(), device.getLatitude(), device.getLatitude(), device.getRegisterTokek(), device.isConnected());
	}
	
	public static List<DeviceDtoOutput> devicesToDTOs( List<Device> devices ) {
		List<DeviceDtoOutput> dtos = new ArrayList<>(devices!=null?devices.size():0);
		if( devices != null ) 
			devices.forEach(d -> dtos.add(deviceToDTO(d)));
		return dtos;
	}

}
