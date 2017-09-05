package org.theiet.rsuite.journals.transforms;

import java.util.ArrayList;
import java.util.List;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.TransformationDirection;
import com.reallysi.rsuite.api.transformation.ManagedObjectTransformer;
import com.reallysi.rsuite.api.transformation.TransformationContext;
import com.reallysi.rsuite.api.transformation.TransformationProvider;

public class IETTransformationProvider implements
		TransformationProvider {

	@Override
	public List<ManagedObjectTransformer> getTransformsForMo(
			ManagedObject mo, TransformationContext context)
			throws RSuiteException {
	    List<ManagedObjectTransformer> results = null;
	    String transformationName = context.getTransformationName();
	    
	    
	    TransformationDirection direction = context.getTransformationDirection();
	    if (direction == TransformationDirection.TO_EXTERNAL)
	    {
	      if (transformationName != null)
	      {	       	        
	        if (transformationName.equalsIgnoreCase("preview"))
	        {
	          results = new ArrayList<ManagedObjectTransformer>();
	          results.add(new HTMLPreviewTransformer(context, mo));	         
	        }
	        
	      }
	    }

	    return results;

	}

}
