package com.rsicms.projectshelper.datatype;

import com.reallysi.rsuite.api.ManagedObject;

public class ManagedObjectWrapper implements Comparable<ManagedObjectWrapper> {

	private ManagedObject mo;

	public ManagedObjectWrapper(ManagedObject mo) {
		this.mo = mo;
	}

	public ManagedObject getMo() {
		return mo;
	}

	@Override
	public int compareTo(ManagedObjectWrapper o) {
		if (o == null) {
			return 1;
		}

		if (mo == null && o.getMo() != null) {
			return -1;
		}

		if (mo != null && o.getMo() == null) {
			return 1;
		}

		return mo.getId().compareTo(o.getMo().getId());
	}

	@Override
	public String toString() {
		if (mo != null) {
			return "moId: " + mo.getId();
		}
		return super.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ManagedObjectWrapper) {
			ManagedObjectWrapper toCompare = (ManagedObjectWrapper) obj;

			int compareResult = compareTo(toCompare);

			return compareResult == 0;
		}

		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (mo != null) {
			return mo.getId().hashCode();
		}
		return super.hashCode();
	}
}
