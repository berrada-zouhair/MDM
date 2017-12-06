package com.mdm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SendMessageDto {
	
	private String message;
	private String reveiverRegisterToken;
	private boolean blockDevice;

}
