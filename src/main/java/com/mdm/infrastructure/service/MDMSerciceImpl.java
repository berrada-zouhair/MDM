package com.mdm.infrastructure.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.mdm.application.dto.DeviceDtoInput;
import com.mdm.domain.models.actions.ACTION_NAME;
import com.mdm.domain.models.actions.Action;
import com.mdm.domain.models.actions.PropertiesNameConst;
import com.mdm.domain.models.entities.Device;
import com.mdm.domain.repository.MDMRepository;
import com.mdm.domain.service.MDMSercice;
import com.mdm.infrastructure.exception.MDMBusinessException;
import com.mdm.interfaces.notifcations.PushyAPI;
import com.mdm.interfaces.notifcations.PushyAPI.PushyPushRequest;

@Service
@Transactional
public class MDMSerciceImpl implements MDMSercice {
	
	public static final int RESULT_CODE_OK = 0;
	public static final int RESULT_CODE_KO = 1;
	
	@Autowired
	private MDMRepository mdmRepository;

	@Override
	public Device connectDeviceToMDM(DeviceDtoInput dto) {
		Device device = mdmRepository.findByImei(dto.getImei());
		if( device == null )
			device = new Device(dto.getIp(), dto.getImei(), dto.getLatitude(), dto.getLongitude(), dto.getRegisterToken());
		else {
			device.setIp(dto.getIp());
			device.setRegisterTokek(dto.getRegisterToken());
			device.setLatitude(dto.getLatitude());
			device.setLongitude(dto.getLongitude());
		}
		device.setConnected(true);
		device.setLastConnection(new Date());
		mdmRepository.save(device);
		Action action = new Action(ACTION_NAME.COONECT_DEVICE, Pair.of(PropertiesNameConst.COONECT_DEVICE_MESSAGE, "success connection"));
		send(new PushyAPI.PushyPushRequest(action, dto.getRegisterToken()));
		return device;
	}

	@Override
	public Device disconnectDeviceFromMDM(DeviceDtoInput dto) throws MDMBusinessException {
		Device device = mdmRepository.findByImei(dto.getImei());
		if(device == null) throw new MDMBusinessException("Unknown device");
		device.setConnected(false);
		mdmRepository.save(device);
		return device;
	}

	@Override
	public Device getDeviceFromMDM(String imei) {
		return mdmRepository.findByImei(imei);
	}

	@Override
	public void receiveActionFromDevice(Action uninstallAction) {
	}

	@Override
	public void executeActionOnDevice(Action action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int send(PushyPushRequest request) {
		try {
			PushyAPI.sendPush(request);
			return RESULT_CODE_OK;
		} catch (Exception e) {
			e.printStackTrace();
			return RESULT_CODE_KO;
		}
	}

	@Override
	public List<Device> getAllDevices() {
		Iterable<Device> iterable = mdmRepository.findAllByOrderByConnectedDesc();
		List<Device> devices = new ArrayList<>();
		iterable.forEach(devices::add);
		return devices;
	}

	@Override
	public List<Device> getConnectedDevices() {
		return mdmRepository.findByConnected(true);
	}

	@Override
	public void updateDevice(Device device) {
		mdmRepository.save(device);
	}

	@Override
	public int sendToAll(Action action) {
		int[] resultCode = {RESULT_CODE_OK};
		List<Device> allDevices = getAllDevices();
		if( allDevices != null ) {
			Consumer<Device> consumer = device -> {
				PushyPushRequest request = new PushyAPI.PushyPushRequest(action, device.getRegisterTokek());
				int sendResultCode = send(request);
				if( resultCode[0] == -1 )
					resultCode[0] = sendResultCode;
				else if( sendResultCode != RESULT_CODE_OK )
					resultCode[0] = RESULT_CODE_KO;
			};
			allDevices.forEach(consumer);
		}
		return resultCode[0];
	}
	
	

}
