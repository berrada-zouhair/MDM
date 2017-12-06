package com.mdm.domaine.models.actions;

import org.junit.Assert;
import org.junit.Test;

import com.mdm.domain.models.actions.ACTION_NAME;
import com.mdm.domain.models.actions.PropertiesNameConst;

/**
 * 
 * Test Unit for the enum {@link #ACTION_NAME}
 *
 */
public class ACTION_NAMETest {
	
	@Test
	public void testEntries() {
		ACTION_NAME action = ACTION_NAME.UNINSTALL_APP;
		Assert.assertEquals(true, action.hasProperties());
		Assert.assertEquals(true, action.hasPropertiy(PropertiesNameConst.APP_NAME));
	}
	

}
