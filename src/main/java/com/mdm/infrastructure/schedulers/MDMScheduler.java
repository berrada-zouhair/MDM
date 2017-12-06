package com.mdm.infrastructure.schedulers;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mdm.domain.models.entities.Device;
import com.mdm.domain.service.MDMSercice;

@Component
public class MDMScheduler {
	
	@Autowired
	private MDMSercice mdmSercice;
	
	private final long REFRESH_DELAY = 30 * 1000;
	private final long DISCONNECTION_DELAY = 30 * 1000;
	
	public MDMScheduler() {
	}
	
	@Scheduled(fixedRate=REFRESH_DELAY)
	public void checkConnectedDevises() {
		List<Device> connectedDevices = mdmSercice.getConnectedDevices();
		Date now = new Date();
		Consumer<Device> consumer = d -> {
			if( now.getTime() - d.getLastConnection().getTime() >= DISCONNECTION_DELAY ) {
				d.setConnected(false);
				mdmSercice.updateDevice(d);
			}
		};
		connectedDevices.forEach(consumer);
	}
	
	

}
