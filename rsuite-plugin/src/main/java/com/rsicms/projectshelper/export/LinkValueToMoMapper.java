package com.rsicms.projectshelper.export;

import net.sf.saxon.om.NodeInfo;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public interface LinkValueToMoMapper {

	ManagedObject mapLinkToMo(ProcessingMessageHandler messageHandler, String contextMoId, NodeInfo attribute) throws RSuiteException;

}
