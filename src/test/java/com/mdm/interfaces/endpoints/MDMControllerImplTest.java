package com.mdm.interfaces.endpoints;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest("com.mdm.interfaces.endpoints")
@Sql("classpath:sql/MDMControllerTest.sql")
public class MDMControllerImplTest {
	
//	@Autowired
//	private MDMControllerImpl controller;
//	
//	private final String registerDevice = "55e4d6268c0be3913694bc";
	
//	@Test
//	public void testGetAllDevices() {
//		List<Device> devices = (List<Device>) controller.getAllDevices().getEntity();
//		Assert.assertNotNull(devices);
//		Assert.assertEquals(devices.size(), 4);
//		Assert.assertEquals(true, devices.get(0).isConnected());
//		Assert.assertEquals(true, devices.get(1).isConnected());
//		Assert.assertEquals(false, devices.get(2).isConnected());
//		Assert.assertEquals(false, devices.get(3).isConnected());
//	}
//	
//	@Test
//	public void testSendMessage() {
//		SendMessageDto dto = new SendMessageDto("message", registerDevice);
//		Response sendMessage = controller.sendMessage(dto);
//		Assert.assertEquals(MDMSerciceImpl.RESULT_CODE_OK, sendMessage.getEntity());
//	}

}
