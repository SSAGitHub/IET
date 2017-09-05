package org.theiet.rsuite.mocks.api.remoteapi;

import java.util.Map;

import com.reallysi.rsuite.api.ConfigurationProperties;
import com.reallysi.rsuite.api.Principal;
import com.reallysi.rsuite.api.Session;
import com.reallysi.rsuite.api.i18n.MessageResources;
import com.reallysi.rsuite.api.remoteapi.MethodType;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.system.RSuiteServerConfiguration;
import com.reallysi.rsuite.service.AuthorizationService;
import com.reallysi.rsuite.service.BackupRestoreService;
import com.reallysi.rsuite.service.BasketService;
import com.reallysi.rsuite.service.BrowseService;
import com.reallysi.rsuite.service.ConfigurationService;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.DomainManager;
import com.reallysi.rsuite.service.EventPublisher;
import com.reallysi.rsuite.service.HotFolderManager;
import com.reallysi.rsuite.service.IDGenerator;
import com.reallysi.rsuite.service.JobManager;
import com.reallysi.rsuite.service.MailService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.reallysi.rsuite.service.MetaDataService;
import com.reallysi.rsuite.service.NotificationManager;
import com.reallysi.rsuite.service.PluginAccessManager;
import com.reallysi.rsuite.service.PluginManager;
import com.reallysi.rsuite.service.ProcessDefinitionService;
import com.reallysi.rsuite.service.ProcessInstanceService;
import com.reallysi.rsuite.service.ProcessManager;
import com.reallysi.rsuite.service.ProductManager;
import com.reallysi.rsuite.service.PubtrackManager;
import com.reallysi.rsuite.service.PurgePolicyManager;
import com.reallysi.rsuite.service.ReportManager;
import com.reallysi.rsuite.service.RepositoryService;
import com.reallysi.rsuite.service.ResourceMonitor;
import com.reallysi.rsuite.service.SchedulerService;
import com.reallysi.rsuite.service.SchemaService;
import com.reallysi.rsuite.service.SearchService;
import com.reallysi.rsuite.service.SecurityService;
import com.reallysi.rsuite.service.SessionService;
import com.reallysi.rsuite.service.StorageManager;
import com.reallysi.rsuite.service.TaskService;
import com.reallysi.rsuite.service.TemplateService;
import com.reallysi.rsuite.service.TransformationManager;
import com.reallysi.rsuite.service.UserService;
import com.reallysi.rsuite.service.XmlApiManager;

public class RemoteApiExecutionContextStub implements RemoteApiExecutionContext {

	@Override
	public Session getSession() {
		return new Session("1111", null);
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthorizationService getAuthorizationService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BackupRestoreService getBackupRestoreService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasketService getBasketService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BrowseService getBrowseService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConfigurationProperties getConfigurationProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConfigurationService getConfigurationService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentAssemblyService getContentAssemblyService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainManager getDomainManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventPublisher getEventPublisher() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HotFolderManager getHotFolderManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDGenerator getIDGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobManager getJobManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MailService getMailService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManagedObjectService getManagedObjectService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageResources getMessageResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetaDataService getMetaDataService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NotificationManager getNotificationManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PluginAccessManager getPluginAccessManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PluginManager getPluginManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessDefinitionService getProcessDefinitionService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstanceService getProcessInstanceService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessManager getProcessManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductManager getProductManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PubtrackManager getPubtrackManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PurgePolicyManager getPurgePolicyManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RSuiteServerConfiguration getRSuiteServerConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportManager getReportManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepositoryService getRepositoryService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceMonitor getResourceMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SchedulerService getSchedulerService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SchemaService getSchemaService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchService getSearchService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SecurityService getSecurityService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionService getSessionService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageManager getStorageManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskService getTaskService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemplateService getTemplateService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransformationManager getTransformationManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserService getUserService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlApiManager getXmlApiManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isShutdownInitiated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public MethodType getMethodType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

}
