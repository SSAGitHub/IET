package org.theiet.rsuite.standards.domain.book.helper;

import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.StringUtils;
import org.theiet.rsuite.standards.StandardsBooksConstans;
import org.theiet.rsuite.standards.utils.O2ProcessingInstructionsUtils;

import com.reallysi.rsuite.api.ContentAssembly;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ManagedObjectReference;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.VersionType;
import com.reallysi.rsuite.api.control.ObjectFreezeOptions;
import com.reallysi.rsuite.api.control.ObjectSource;
import com.reallysi.rsuite.api.control.XmlObjectSource;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ContentAssemblyService;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.helpers.tree.impl.TreeDescendingContentAssemblyVisitorBase;

public class BookEditionContentArchiver extends
		TreeDescendingContentAssemblyVisitorBase implements
		StandardsBooksConstans {

	private ContentAssemblyService caSvc;

	private ManagedObjectService moSvc;

	public BookEditionContentArchiver(ExecutionContext context, User user) {
		super(context, user);

		caSvc = context.getContentAssemblyService();
		moSvc = context.getManagedObjectService();
	}

	public void archive(ContentAssembly typeScript) throws RSuiteException {
		visitContentAssembly(typeScript);
	}

	@Override
	public void visitManagedObjectReference(ManagedObjectReference moRef)
			throws RSuiteException {

		ManagedObject mo = moSvc.getManagedObject(getUser(), moRef.getId());

		ManagedObject srcMo = moSvc.getManagedObject(getUser(),
				moRef.getTargetId());

		if (StringUtils.isBlank(moRef.getTargetRevision())) {
			pinMoToSpecificVersion(mo);
		}

		acceptAllChanges(srcMo);

	}

	@Override
	public void visitManagedObject(ManagedObject mo) throws RSuiteException {
	}

	private void pinMoToSpecificVersion(ManagedObject mo)
			throws RSuiteException {
		caSvc.freeze(getUser(), mo.getId(),
				new ObjectFreezeOptions(mo.getRevision()));

	}

	private void acceptAllChanges(ManagedObject mo) throws RSuiteException {

		if (mo.isNonXml()){
			return;
		}
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		boolean hasPI = O2ProcessingInstructionsUtils.removeO2ChangeTrackingPI(
				mo.getInputStream(), outStream);

		if (hasPI) {

			moSvc.checkOut(getUser(), mo.getId());

			ObjectSource src = new XmlObjectSource(outStream.toByteArray());

			moSvc.load(getUser(), src, null);

			moSvc.checkIn(getUser(), mo.getId(), VersionType.MAJOR,
					"Removed change tracking information", true);

		}

	}

}
