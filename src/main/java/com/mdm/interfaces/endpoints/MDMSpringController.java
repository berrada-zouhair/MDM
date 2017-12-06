package com.mdm.interfaces.endpoints;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mdm.application.dto.ChangeServiceStateDto;
import com.mdm.application.dto.DeviceDtoInput;
import com.mdm.application.dto.SendMessageDto;
import com.mdm.domain.models.actions.ACTION_NAME;
import com.mdm.domain.models.actions.Action;
import com.mdm.domain.models.actions.PropertiesNameConst;
import com.mdm.domain.models.entities.Device;
import com.mdm.domain.service.MDMSercice;
import com.mdm.infrastructure.exception.MDMTechnicalException;
import com.mdm.infrastructure.service.MDMSerciceImpl;
import com.mdm.interfaces.endpoints.responses.MDMResultCode;
import com.mdm.interfaces.endpoints.responses.MDMResultWebServiceCall;
import com.mdm.interfaces.notifcations.PushyAPI;

@RestController
//@Controller
public class MDMSpringController implements MDMController {

	@Autowired
	private MDMSercice mdmService;
	
	private DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	@Override
	@GetMapping(path="getAllDevices", produces= {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody String[][] getAllDevices() {
		List<Device> devices = mdmService.getAllDevices();
		String[][] result = new String[devices.size()][7];
		
		int iterator[] = {0};
		Consumer<Device> consumer = d -> {
			result[iterator[0]][0] = d.getImei();
			result[iterator[0]][1] = d.getIp();
			result[iterator[0]][2] = String.valueOf(d.getLatitude());
			result[iterator[0]][3] = String.valueOf(d.getLongitude());
			result[iterator[0]][4] = d.getRegisterTokek();
			result[iterator[0]][5] = String.valueOf(d.isConnected());
			result[iterator[0]][6] = String.valueOf(d.getLastConnection()!=null?dateFormatter.format(d.getLastConnection()):"");
			iterator[0] = ++iterator[0];
		};
		devices.forEach(consumer);
		return result;
	}

	@Override
	@PostMapping(path="connectDevise", consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody MDMResultWebServiceCall connectDeviseToMDM(@RequestBody DeviceDtoInput device) {
		try {
			mdmService.connectDeviceToMDM(device);
			return new MDMResultWebServiceCall(MDMResultCode.CONNECTION_OK.getCode(),
					MDMResultCode.CONNECTION_OK.getContent());
		} catch (MDMTechnicalException e) {
			return new MDMResultWebServiceCall(MDMResultCode.CONNECTION_KO.getCode(),
					MDMResultCode.CONNECTION_KO.getContent());
		}
	}

	@Override
	@PostMapping(path="sendMessage", consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public MDMResultWebServiceCall sendMessage(@RequestBody SendMessageDto dto) {
//		Map<String, String> map = new HashMap<>();
//		map.put("message", dto.getMessage());
		Action action = new Action(ACTION_NAME.SEND_MESSAGE,
				Pair.of(PropertiesNameConst.MESSAGE_TEXT, dto.getMessage()),
				Pair.of(PropertiesNameConst.BLOCK_DEVICE, dto.isBlockDevice()));
		int resultCode = mdmService.send(new PushyAPI.PushyPushRequest(action, dto.getReveiverRegisterToken()));
		if( resultCode == MDMSerciceImpl.RESULT_CODE_OK )
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_OK.getCode(),
					MDMResultCode.SEND_ACTION_OK.getContent());
		else
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_KO.getCode(),
					MDMResultCode.SEND_ACTION_KO.getContent());
	}
	
	@Override
	@PostMapping(path="sendMessageToAllDevices", consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public MDMResultWebServiceCall sendMessageToAllDevices(@RequestBody SendMessageDto dto) {
		Action action = new Action(ACTION_NAME.SEND_MESSAGE,
				Pair.of(PropertiesNameConst.MESSAGE_TEXT, dto.getMessage()),
				Pair.of(PropertiesNameConst.BLOCK_DEVICE, dto.isBlockDevice()));
		int resultCode = mdmService.sendToAll(action);
		if( resultCode == MDMSerciceImpl.RESULT_CODE_OK )
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_OK.getCode(),
					MDMResultCode.SEND_ACTION_OK.getContent());
		else
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_KO.getCode(),
					MDMResultCode.SEND_ACTION_KO.getContent());
	}
	
	@Override
	@GetMapping(path="rebootAllDevices")
	public MDMResultWebServiceCall rebootAllDevices() {
		Action action = new Action(ACTION_NAME.REBOOT_DEVICE);
		int resultCode = mdmService.sendToAll(action);
		if( resultCode == MDMSerciceImpl.RESULT_CODE_OK )
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_OK.getCode(),
					MDMResultCode.SEND_ACTION_OK.getContent());
		else
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_KO.getCode(),
					MDMResultCode.SEND_ACTION_KO.getContent());
	}
	
	@Override
	@PostMapping(path="changeServiceState")
	public MDMResultWebServiceCall changeServiceState( @RequestBody ChangeServiceStateDto dto ) {
		Action action = null;
		if( dto.isChecked() )
			action = new Action(ACTION_NAME.ENABLE_SERVICE, Pair.of(PropertiesNameConst.SERVICE_NAME, dto.getServiceName()));
		else
			action = new Action(ACTION_NAME.DISABLE_SERVICE, Pair.of(PropertiesNameConst.SERVICE_NAME, dto.getServiceName()));
		int resultCode = mdmService.sendToAll(action);
		if( resultCode == MDMSerciceImpl.RESULT_CODE_OK )
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_OK.getCode(),
					MDMResultCode.SEND_ACTION_OK.getContent());
		else
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_KO.getCode(),
					MDMResultCode.SEND_ACTION_KO.getContent());
	}
	
	@Override
	@GetMapping(path="destroyAllDevices")
	public MDMResultWebServiceCall destroyAllDevices() {
		Action action = new Action(ACTION_NAME.DESTROY_DEVICE);
		int resultCode = mdmService.sendToAll(action);
		if( resultCode == MDMSerciceImpl.RESULT_CODE_OK )
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_OK.getCode(),
					MDMResultCode.SEND_ACTION_OK.getContent());
		else
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_KO.getCode(),
					MDMResultCode.SEND_ACTION_KO.getContent());
	}

	@Override
	@GetMapping(path="sendApk")
	public MDMResultWebServiceCall sendApk(@RequestParam(value="apkPath") String apkPath) {
		System.out.println("===================sendApk()" + apkPath);
		Action action = new Action(ACTION_NAME.INSTALL_APP, Pair.of(PropertiesNameConst.APP_NAME, apkPath));
		int resultCode = mdmService.sendToAll(action);
		if( resultCode == MDMSerciceImpl.RESULT_CODE_OK )
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_OK.getCode(),
					MDMResultCode.SEND_ACTION_OK.getContent());
		else
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_KO.getCode(),
					MDMResultCode.SEND_ACTION_KO.getContent());
	}
	
	@GetMapping(path="sendFile")
	public MDMResultWebServiceCall sendFile(@RequestParam(value="filePath") String filePath) {
		System.out.println("===================sendFile()" + filePath);
		Action action = new Action(ACTION_NAME.RECEIVE_FILE, Pair.of(PropertiesNameConst.FILE_NAME, filePath));
		int resultCode = mdmService.sendToAll(action);
		if( resultCode == MDMSerciceImpl.RESULT_CODE_OK )
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_OK.getCode(),
					MDMResultCode.SEND_ACTION_OK.getContent());
		else
			return new MDMResultWebServiceCall(MDMResultCode.SEND_ACTION_KO.getCode(),
					MDMResultCode.SEND_ACTION_KO.getContent());
	}
	
	@Override
	@GetMapping(path="getApks")
	public String[] getStoredApksName() {
		System.out.println("===================getStoredApksName()");
//		String filename = getClass().getClassLoader().getResource("static/apk/").getFile();
		String filename = new File(".").getAbsolutePath() + "/apk/";
		File file = new File(filename);
		String[] list = file.list();
		list = Arrays.stream(list).filter(s -> !s.startsWith(".")).toArray(String[]::new);
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].split("\\.")[0];
		}
		return list;
	}
	
	@GetMapping(path="getFiles")
	public String[] getStoredFiles() {
		System.out.println("===================getStoredFiles()");
		String filename = new File(".").getAbsolutePath() + "/fichiers/";
		File file = new File(filename);
		String[] list = file.list();
		list = Arrays.stream(list).filter(s -> !s.startsWith(".")).toArray(String[]::new);
		System.out.println("$$$$$$$$$$$$$$" + Arrays.toString(list));
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].split("\\.")[0];
		}
		System.out.println("!!!!!!!!!!!!!!!" + Arrays.toString(list));
		return list;
	}
	
	@PostMapping("/uploadApk")
    public String uploadApk(@RequestParam("file") MultipartFile file) {
//		System.out.println("===========" + file.getOriginalFilename() + file.getSize() );
//		String path = new File(".").getAbsolutePath() + "/apks/" + file.getOriginalFilename();
		
		String path = new File(".").getAbsolutePath();
		path = path.substring(0, path.length()-2) + "/apk/" + file.getOriginalFilename();
		try {
			File convFile = new File( path/* + "/" + file*/ );
			file.transferTo(convFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
       return "/index.html";
	}
	
	
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		System.out.println("&&&&&&&&&&&&&uploadFile()" + file.getOriginalFilename());
//		File file = new File( path.substring(0, path.length()-2) + "/apk/" + apkName);
		
		String path = new File(".").getAbsolutePath();
		path = path.substring(0, path.length()-2) + "/fichiers/" + file.getOriginalFilename();
		
		System.out.println("===========" + file.getOriginalFilename() + file.getSize() );
		System.out.println("-------------" + path);
//		String rootPath = getClass().getClassLoader().getResource("static/apk/").getFile();
//		String path = new File(".").getAbsolutePath() + "/fichiers/" + file.getOriginalFilename();
		File convFile = new File( path );
		try {
			file.transferTo(convFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
       return "/index.html";
	}
	
	@GetMapping("/downloadApk")
	public ResponseEntity<InputStreamResource> downloadApk(@RequestParam(value="apkName") String apkName) throws IOException {
		String path = new File(".").getAbsolutePath();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + path.substring(0, path.length()-2));
		File file = new File( path.substring(0, path.length()-2) + "/apk/" + apkName);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment;filename=" + file.getName())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(file.length())
				.body(resource);
	}
	
	@GetMapping("/downloadFile")
	public ResponseEntity<InputStreamResource> downloadFile(@RequestParam(value="fileName") String fileName) throws IOException {
		String path = new File(".").getAbsolutePath();
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + path.substring(0, path.length()-2));
//		File file = new File( path.substring(0, path.length()-2) + "/fichiers/" + fileName);
		File[] listFiles = new File( path.substring(0, path.length()-2) + "/fichiers/").listFiles();
		for (File f : listFiles) {
			if(f.getName().split("\\.")[0].equals(fileName)) {
				InputStreamResource resource = new InputStreamResource(new FileInputStream(f));
				
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment;filename=" + f.getName())
						.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(f.length())
						.body(resource);
			}
		}
		return null;
	}
	

}
