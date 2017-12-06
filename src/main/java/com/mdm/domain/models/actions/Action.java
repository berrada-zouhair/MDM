package com.mdm.domain.models.actions;

import java.io.Serializable;

import org.springframework.data.util.Pair;

import lombok.Data;

/** Class that represents Action to process and its details */
@Data
public class Action implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ACTION_NAME actionName;
	private Pair<String, Object>[] actionDetails;
	
	@SafeVarargs
	public Action(ACTION_NAME actionName, Pair<String, Object>... actionDetails) {
		this.actionName = actionName;
		this.actionDetails = actionDetails;
		
	}

}
