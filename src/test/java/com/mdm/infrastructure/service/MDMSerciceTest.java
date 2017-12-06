package com.mdm.infrastructure.service;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.mdm.application.dto.DeviceDtoInput;
import com.mdm.domain.models.actions.ACTION_NAME;
import com.mdm.domain.models.actions.Action;
import com.mdm.domain.models.actions.PropertiesNameConst;
import com.mdm.domain.models.entities.Device;
import com.mdm.domain.service.MDMSercice;
import com.mdm.infrastructure.exception.MDMBusinessException;
import com.mdm.interfaces.notifcations.PushyAPI;

/**
 * Unit Test for {@link #MDMService}
 */
@RunWith(SpringRunner.class)
@SpringBootTest("com.mdm.infrastructure.service")
@Sql("classpath:sql/MDMSerciceTest.sql")
public class MDMSerciceTest {
	
	@Autowired
	private MDMSercice mdmService;
	
	@Test
	public void testConnectDeviseFirstTime() {
		DeviceDtoInput dto = new DeviceDtoInput("ip", "imei1", 10D, 11D, "registerToken1");
		Device device = mdmService.connectDeviceToMDM(dto);
		Assert.assertNotNull(device);
		Assert.assertEquals(true, device.isConnected());
	}
	
	@Test
	public void testConnectDeviseAlreadyExisting() {
		//check if it exists
		DeviceDtoInput dto = new DeviceDtoInput("ip", "imei2", 11D, 11D, "registerToken2");
		Device device = mdmService.getDeviceFromMDM(dto.getImei());
		Assert.assertNotNull(device);
		mdmService.connectDeviceToMDM(dto);
		assertEquals("ip", dto.getIp());
		assertEquals((Double)11D, dto.getLatitude());
		assertEquals((Double)11D, dto.getLongitude());
	}
	
	@Test
	public void testDisconnectExistingDevise() throws MDMBusinessException {
		DeviceDtoInput dto = new DeviceDtoInput("ip", "imei2", 10D, 11D, "registerToken2");
		Device device = mdmService.disconnectDeviceFromMDM(dto);
		Assert.assertNotNull(device);
		Assert.assertEquals(false, device.isConnected());
	}
	
	@Test(expected=MDMBusinessException.class)
	public void testnonExistingDevise() throws MDMBusinessException {
		DeviceDtoInput dto = new DeviceDtoInput("ip", "imei", 10D, 11D, "registerToken1");
		mdmService.disconnectDeviceFromMDM(dto);
	}
	
//	@Test
//	public void sendActionUninstallApp() {
//		Pair<String, Object> appToUninstall = Pair.of("app.name", "Facebook");
//		ACTION_NAME actionName = ACTION_NAME.UNINSTALL_APP;
//		Action uninstallAction = new Action(actionName, appToUninstall);
//		mdmService.sendAction(uninstallAction);
//	}
	
//	@Test
//	public void receiveActionReceiveLogs() {
//		Pair<String, Object> logsDate = Pair.of(PropertiesNameConst.LOGS_DATE, new Date());
//		Pair<String, Object> logsText = Pair.of(PropertiesNameConst.LOGS_TEXT, "logs");
//		Action uninstallAction = new Action(ACTION_NAME.RECEIVE_LOGS, logsDate, logsText);
//		mdmService.receiveActionFromDevice(uninstallAction);
//	}
	
	@Test
	public void testSendMessage() {
		Device device = mdmService.getDeviceFromMDM("imei2");
		String message = "Message to send";
		Action messageAction = new Action(ACTION_NAME.SEND_MESSAGE, Pair.of(PropertiesNameConst.MESSAGE_TEXT, message));
		PushyAPI.PushyPushRequest request = new PushyAPI.PushyPushRequest(messageAction, device.getRegisterTokek());
		int resultCode = mdmService.send( request );
		Assert.assertEquals(resultCode, MDMSerciceImpl.RESULT_CODE_OK);
	}

}
