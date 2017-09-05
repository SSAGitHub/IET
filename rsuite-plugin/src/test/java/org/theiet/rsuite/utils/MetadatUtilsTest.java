package org.theiet.rsuite.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import com.reallysi.rsuite.api.MetaDataItem;
import com.reallysi.rsuite.api.remoteapi.CallArgument;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.rsicms.projectshelper.lmd.MetadataUtils;

public class MetadatUtilsTest {

	@Test
	public void lmdListFromWsParameters_noParameters_emptyLmdList() {

		CallArgumentList argumentListStub = createArgumentListStub(new ArrayList<CallArgument>());

		List<MetaDataItem> lmdListFromWsParameters = MetadataUtils
				.getLmdListFromWsParameters(argumentListStub);
		Assert.assertEquals(0, lmdListFromWsParameters.size());
	}

	@Test
	public void lmdListFromWsParameters_nolmdParameter_emptyLmdList() {

		String lmdName = "bookTitle";
		String lmdValue = "Sample title";
		
		String parameterName = lmdName;

		List<CallArgument> arguments = new ArrayList<CallArgument>();
		arguments.add(new CallArgument(parameterName, lmdValue));
		
		CallArgumentList argumentListStub = createArgumentListStub(arguments);

		List<MetaDataItem> lmdListFromWsParameters = MetadataUtils
				.getLmdListFromWsParameters(argumentListStub);

		Assert.assertEquals(0, lmdListFromWsParameters.size());
	}
	
	@Test
	public void lmdListFromWsParameters_lmdParameter_lmdListWithOneLmd() {

		String lmdName = "bookTitle";
		String lmdValue = "Sample title";
		
		String parameterName = "lmd_" + lmdName;

		List<CallArgument> arguments = new ArrayList<CallArgument>();
		arguments.add(new CallArgument(parameterName, lmdValue));
		
		CallArgumentList argumentListStub = createArgumentListStub(arguments);

		List<MetaDataItem> lmdListFromWsParameters = MetadataUtils
				.getLmdListFromWsParameters(argumentListStub);

		MetaDataItem metadataItem = lmdListFromWsParameters.get(0);
		
		Assert.assertEquals(lmdName, metadataItem.getName());
		Assert.assertEquals(lmdValue, metadataItem.getValue());
	}

	@Test
	public void lmdListFromWsParameters_lmdMultiValueParameter_lmdListWithOneLmd() {

		String lmdName = "bookTitle";
		String lmdValue = "Sample title";
		String lmdValue2 = "Another value";
		
		String parameterName = "lmd_" + lmdName;

		List<CallArgument> arguments = new ArrayList<CallArgument>();
		arguments.add(new CallArgument(parameterName, lmdValue));
		arguments.add(new CallArgument(parameterName, lmdValue2));
		
		CallArgumentList argumentListStub = createArgumentListStub(arguments);

		List<MetaDataItem> lmdListFromWsParameters = MetadataUtils
				.getLmdListFromWsParameters(argumentListStub);

		Assert.assertEquals(2, lmdListFromWsParameters.size());
		
	}
	
	public CallArgumentList createArgumentListStub(List<CallArgument> arguments) {
		CallArgumentList argumentListStub = Mockito
				.mock(CallArgumentList.class);
		

		Mockito.when(argumentListStub.getAll()).thenReturn(arguments);
		return argumentListStub;
	}

}
