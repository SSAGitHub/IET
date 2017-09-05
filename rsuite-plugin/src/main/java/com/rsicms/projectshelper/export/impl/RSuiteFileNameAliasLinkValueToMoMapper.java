package com.rsicms.projectshelper.export.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.saxon.om.NodeInfo;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.projectshelper.datatype.ManagedObjectWrapper;
import com.rsicms.projectshelper.export.LinkValueToMoMapper;
import com.rsicms.projectshelper.message.ProcessingMessageHandler;

public class RSuiteFileNameAliasLinkValueToMoMapper implements
		LinkValueToMoMapper {

	private static final String RSUITE_REST_ELEMENT_ALIAS = "/rsuite/rest/v2/content/element/alias/";

	private static final String RSUITE_REST_BINARY_ALIAS = "/rsuite/rest/v2/content/binary/alias/";

	private ManagedObjectService moService;

	private User user;

	private Map<String, ManagedObjectWrapper> cache = new HashMap<String, ManagedObjectWrapper>();

	private String contextMoId;

	private ManagedObject contextMo;

	public RSuiteFileNameAliasLinkValueToMoMapper(User user,
			ManagedObjectService moService) {
		this.user = user;
		this.moService = moService;
	}

	@Override
	public ManagedObject mapLinkToMo(ProcessingMessageHandler messageHandler, String contextMoId, NodeInfo attribute)
			throws RSuiteException {

		String linkValue = attribute.getStringValue();

		if (StringUtils.isBlank(linkValue)) {
			return null;
		}

		if (linkValue.startsWith("#")) {

			if (contextMoId.equals(this.contextMoId) && contextMo != null) {
				return contextMo;
			}

			contextMo = moService.getManagedObject(user, contextMoId);
			this.contextMoId = contextMoId;

			return contextMo;
		}

		String alias = getAliasFromLinkValue(linkValue);

		if (cache.containsKey(alias)) {
			return cache.get(alias).getMo();
		}
		
		if (StringUtils.isBlank(alias)){
		    return null;
		}

		ManagedObject referncedMo = getManagedObjectFromRSuite(messageHandler, alias);

		return referncedMo;

	}

	public ManagedObject getManagedObjectFromRSuite(ProcessingMessageHandler messageHandler, String alias)
			throws RSuiteException {
		List<ManagedObject> referncedMos = moService.getObjectsByAlias(user,
				alias);
		
		ManagedObject referncedMo = null;

		if (referncedMos.size() > 1) {
			reportDuplicatedAliasProblem(messageHandler, referncedMos, alias);
		} else if (referncedMos.size() == 1) {
			referncedMo = referncedMos.get(0);
			referncedMo = moService.getManagedObject(user, referncedMo.getId());
		}

		cache.put(alias, new ManagedObjectWrapper(referncedMo));
		return referncedMo;
	}

	private void reportDuplicatedAliasProblem(ProcessingMessageHandler messageHandler, List<ManagedObject> referncedMos,
			String alias) {
		StringBuilder errorMessage = new StringBuilder(
				"There is more than one MO with alias ");
		errorMessage.append(alias).append(" mo ids: ");

		for (ManagedObject mo : referncedMos) {
			errorMessage.append(" ").append(mo.getId());
		}

		messageHandler.error(errorMessage.toString());
	}

	protected static String getAliasFromLinkValue(String nodeValue) {
		String alias = nodeValue.replace(RSUITE_REST_BINARY_ALIAS, "");
		alias = alias.replace(RSUITE_REST_ELEMENT_ALIAS, "");
		int hashIndex = alias.indexOf('#');

		if (hashIndex > -1) {
			alias = alias.substring(0, hashIndex);
		}

		return alias;
	}

}
