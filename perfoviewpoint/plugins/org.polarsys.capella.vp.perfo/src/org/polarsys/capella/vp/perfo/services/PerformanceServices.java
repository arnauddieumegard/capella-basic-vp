/*******************************************************************************
 * Copyright (c) 2006, 2015 Thales Global Services
 *   This program and the accompanying materials are made available under the
 *   terms of the Eclipse Public License 2.0 which is available at
 *   http://www.eclipse.org/legal/epl-2.0
 *   
 *   SPDX-License-Identifier: EPL-2.0
 * 
 *   Contributors:
 *      Thales - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.perfo.services;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.capellacore.InvolvedElement;
import org.polarsys.capella.core.data.capellacore.NamedElement;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.vp.perfo.generic.GenericPerformanceService;
import org.polarsys.capella.vp.perfo.generic.IPerformanceBrowser;
import org.polarsys.capella.vp.perfo.perfo.PerformanceCriteria;
import org.polarsys.capella.vp.perfo.perfo.TimeCapacity;
import org.polarsys.capella.vp.perfo.perfo.TimeConsumption;


public class PerformanceServices extends GenericPerformanceService implements IPerformanceBrowser{
	
	public PerformanceServices(){
		super();
		super.setBrowser(this);
	}
	
	public int checkPerformance(EObject perfoCriteriaObject, EObject container) {
		if (! (container instanceof NamedElement)){
			return 0;
		}
		super.setBrowser(this);
		
		int current = super.checkPerformance(container);
			
		((TimeCapacity)perfoCriteriaObject).setCurrentExecutionTime(current);
		
		return current;
	}
	
	public EList<EObject> getLeafsObjects(EObject eObject) {
		BasicEList<EObject> list = new BasicEList<EObject>();
		
		if (eObject instanceof FunctionalChain){
			for (InvolvedElement iEO : ((FunctionalChain)eObject).getInvolvedElements()) {
				list.add((EObject)iEO);
			}
			return list;
		} 
//		if (eObject instanceof Function ||
//				eObject instanceof FunctionalExchange ||
//				eObject instanceof ForkFunction ||
//				eObject instanceof JoinFunction){
//			return null;
//		}
//		
//		if (eObject instanceof FunctionalExchange){
//			
//			FunctionalExchange fEx = (FunctionalExchange) eObject;
//			fEx.getInvolvingFunctionalChains();
//			
//			return null;
//		}
		
		return null;
	}

	public EList<EObject> getSubComponentsObjects(EObject eObject) {
		return new BasicEList<EObject>();
	}

	public EObject getPerformanceCriteriaObject(EObject container) {
		if (container instanceof PerformanceCriteria){
			return container;
		}else{
			/*
			 * The eObject parameter is a direct container of a PerformanceCriteria Object, then
			 * look for the PerformanceCriteria Object in the eObject.eContents() set.
			 */

			for (EObject curChild : container.eContents()) {
				if (curChild instanceof PerformanceCriteria){
					return curChild;
				}
			}
		}
		return null;
	}

	public EObject getPerformanceCriteriaObject(EObject container,
			int PerformanceCriteriaKind) {
		EObject o = getPerformanceCriteriaObject(container);
		switch (PerformanceCriteriaKind) {
		case PerformanceCapacityObject :
			if (o instanceof TimeCapacity){
				return o;
			}else{
				return null;
			}

		case PerformanceConsumptionObject :
			if (o instanceof TimeConsumption){
				return o;
			}else{
				return null;
			}
		}
		return null;
	}

}
