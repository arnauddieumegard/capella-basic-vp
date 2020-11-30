/*******************************************************************************
 * Copyright (c) 2006, 2020 Thales Global Services
 *   This program and the accompanying materials are made available under the
 *   terms of the Eclipse Public License 2.0 which is available at
 *   http://www.eclipse.org/legal/epl-2.0
 *   
 *   SPDX-License-Identifier: EPL-2.0
 * 
 *   Contributors:
 *      Thales - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.mass.helpers;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.capellacore.NamedElement;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.pa.PhysicalArchitecture;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalComponentPkg;
import org.polarsys.capella.vp.mass.mass.Mass;

public class MassHelper {

	private static final int Mass = 2;

	/**
	 * Check if a given model element is the Physical system.
	 * 
	 * @param eObject a model element
	 * @return <code> True </code> if the element is the Physical System,
	 *         <code> False </code> otherwise.
	 */
	public static final boolean isPhysicalSystem(EObject eObject) {
		return eObject instanceof PhysicalComponent && eObject.eContainer() instanceof PhysicalComponentPkg;
	}

	public EList<EObject> getRootMassObjects(EObject eO) {
		EList<EObject> list = new BasicEList<EObject>();
		if (eO instanceof NamedElement) {
			PhysicalComponent pc = (PhysicalComponent) eO;
			Part part = (Part) pc.getAbstractTypedElements().get(0);
			list.add(part);
			// list.addAll(getWPObjects(part, Weight));
		}
		return list;
	}

	/**
	 * Return all Price and Weight objects defined for a given Part or Physical
	 * Component
	 * 
	 * @param eObject : A Part or a Physical Component
	 * @return
	 */
	public EList<EObject> getMassObjects(EObject eObject) {
		EList<EObject> resultat = new BasicEList<EObject>();
		resultat.addAll((getMassObjects(eObject, Mass)));
		return resultat;
	}

	public EList<EObject> getMassObject(EObject eObject) {
		return getMassObjects(eObject, Mass);
	}

	/**
	 * This method is useful when the Melody Advance project is defined as a mono
	 * part project. It get the first part defined for a Physical Component. Only
	 * the first because it contain only one.
	 * 
	 * @param container
	 * @return
	 */
	private EObject getCorrectContainer(EObject container) {
		if (!(container instanceof Part || container instanceof PhysicalComponent
				|| container instanceof PhysicalComponentPkg)) {
			return null;
		}
		if (container instanceof PhysicalComponentPkg) {
			PhysicalComponentPkg pkg = (PhysicalComponentPkg) container;
			return pkg.getOwnedParts().get(0);
		} else if (container instanceof PhysicalComponent) {
			PhysicalComponent pc = (PhysicalComponent) container;
			if (!pc.getAbstractTypedElements().isEmpty()) {
				return pc.getAbstractTypedElements().get(0);
			}
		}
		return container;
	}

	private EList<EObject> getMassObjects(EObject eObject, int kind) {

		EObject obj = getCorrectContainer(eObject);
		if (obj == null) {
			return (EList<EObject>) ECollections.EMPTY_ELIST;
		}

		EList<EObject> resulat = new BasicEList<EObject>();

		for (EObject i : obj.eContents()) {
			if (kind == Mass) {
				if (i instanceof Mass) {
					resulat.add(i);
				}
			}
		}

		return resulat;
	}

}
