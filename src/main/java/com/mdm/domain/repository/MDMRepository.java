package com.mdm.domain.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.mdm.domain.models.entities.Device;

public interface MDMRepository extends PagingAndSortingRepository<Device, Long> {
	
	public Device findByImei( String imei );
	
	public List<Device> findAllByOrderByConnectedDesc();

	public List<Device> findByConnected(boolean b);

}
