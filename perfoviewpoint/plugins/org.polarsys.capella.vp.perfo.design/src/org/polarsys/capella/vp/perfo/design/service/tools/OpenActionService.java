/******************************************************************************
* Copyright (c) 2006, 2020 Thales Global Services 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0 
 * 
 * Contributors: 
 *    Thales - initial API and implementation
*****************************************************************************/
package org.polarsys.capella.vp.perfo.design.service.tools;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.vp.perfo.perfo.PerfoFactory;
import org.polarsys.capella.vp.perfo.perfo.TimeCapacity;
import org.polarsys.capella.vp.perfo.perfo.TimeConsumption;
import org.polarsys.kitalpha.emde.model.ElementExtension;

/**
 * <!-- begin-user-doc -->
 * This class is an implementation of the DoReMi JavaExtension '<em><b>[org.polarsys.capella.vp.perfo.design.service.tools.OpenActionService]</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */

public class OpenActionService {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param element : the element
	 * @param newSemanticContainer : the element view
	 * @generated NOT
	 */
	public boolean CreateLABFunctionTimeConsumption(EObject element, EObject newSemanticContainer) {
		if (element instanceof AbstractFunction
				&& checktimeConsumptionUnicity(((AbstractFunction) element).getOwnedExtensions())) {
			AbstractFunction functionalExchange = (AbstractFunction) element;
			TimeConsumption timeConsumption = PerfoFactory.eINSTANCE.createTimeConsumption();
			timeConsumption.setId(EcoreUtil.generateUUID());
			timeConsumption.setValue(0);
			functionalExchange.getOwnedExtensions().add(timeConsumption);
			return true;
		}

		if (element instanceof FunctionalExchange
				&& checktimeConsumptionUnicity(((FunctionalExchange) element).getOwnedExtensions())) {
			FunctionalExchange functionalExchange = (FunctionalExchange) element;
			TimeConsumption timeConsumption = PerfoFactory.eINSTANCE.createTimeConsumption();
			timeConsumption.setId(EcoreUtil.generateUUID());
			timeConsumption.setValue(0);
			functionalExchange.getOwnedExtensions().add(timeConsumption);
			return true;
		}

		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param element : the element
	 * @param newSemanticContainer : the element view
	 * @generated NOT
	 */
	public boolean createFunctionalCapacity(EObject element, EObject newSemanticContainer) {
		if (element instanceof FunctionalChain
				&& checktimeCapacityUnicity(((FunctionalChain) element).getOwnedExtensions())) {
			FunctionalChain functionalChain = (FunctionalChain) element;
			TimeCapacity timeCapacity = PerfoFactory.eINSTANCE.createTimeCapacity();
			timeCapacity.setId(EcoreUtil.generateUUID());
			timeCapacity.setValue(0);
			functionalChain.getOwnedExtensions().add(timeCapacity);
			return true;
		}
		return false;
	}

	private boolean checktimeConsumptionUnicity(EList<ElementExtension> extensions) {

		for (ElementExtension elementExtension : extensions) {
			if (elementExtension instanceof TimeConsumption)
				return false;
		}
		return true;
	}

	private boolean checktimeCapacityUnicity(EList<ElementExtension> extensions) {

		for (ElementExtension elementExtension : extensions) {
			if (elementExtension instanceof TimeCapacity)
				return false;
		}
		return true;
	}

}