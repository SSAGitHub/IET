package org.theiet.rsuite.standards.domain.book.helper;

import java.util.Stack;

import org.theiet.rsuite.standards.StandardsBooksConstans;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.ContentAssemblyReference;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.control.ContentAssemblyCreateOptions;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.helpers.tree.impl.TreeDescendingContentAssemblyVisitorBase;

public class BookEditionContentCopier extends
		TreeDescendingContentAssemblyVisitorBase implements
		StandardsBooksConstans {

	private ContentAssemblyService caSvc;

	private ManagedObjectService moSvc;

	private Stack<String> caStack = new Stack<String>();

	public BookEditionContentCopier(ExecutionContext context, User user,
			ContentAssembly targetTypeScript) {
		super(context, user);
		caSvc = context.getContentAssemblyService();
		moSvc = context.getManagedObjectService();
		caStack.push(targetTypeScript.getId());
	}

	public void copyContent(ContentAssembly typeScriptFrom)
			throws RSuiteException {
		visitContentAssembly(typeScriptFrom);
	}

	@Override
	public void visitContentAssemblyNodeContainer(
			ContentAssemblyNodeContainer container) throws RSuiteException {

		String caType = container.getType();

		if (!CA_TYPE_STANDARDS_TYPESCRIPT.equals(caType)) {
			createNewCa(container, caType);
		}

		super.visitContentAssemblyNodeContainer(container);

		caStack.pop();
	}

	private void createNewCa(ContentAssemblyNodeContainer container,
			String caType) throws RSuiteException {
		String name = container.getDisplayName();

		String parentId = caStack.peek();

		ContentAssemblyCreateOptions createOptions = new ContentAssemblyCreateOptions();
		createOptions.setType(caType);
		createOptions.setSilentIfExists(true);

		ContentAssembly newCa = caSvc.createContentAssembly(getUser(),
				parentId, name, createOptions);

		caStack.push(String.valueOf(newCa.getId()));
	}

	@Override
	public void visitContentAssemblyReference(ContentAssemblyReference caRef)
			throws RSuiteException {
		super.visitContentAssemblyReference(caRef);
	}

	@Override
	public void visitManagedObjectReference(ManagedObjectReference moRef)
			throws RSuiteException {

		ManagedObject srcMo = moSvc.getManagedObject(getUser(),
				moRef.getTargetId());

		String contentAssemblyId = caStack.peek();
		caSvc.attach(getUser(), contentAssemblyId, srcMo, null);

	}

	@Override
	public void visitManagedObject(ManagedObject mo) throws RSuiteException {
	}

}
