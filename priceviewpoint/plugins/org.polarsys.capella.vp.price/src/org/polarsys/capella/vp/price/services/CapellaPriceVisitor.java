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
package org.polarsys.capella.vp.price.services;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.capellacore.Feature;
import org.polarsys.capella.core.data.cs.AbstractDeploymentLink;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.model.handler.helpers.CapellaProjectHelper;
import org.polarsys.capella.core.model.handler.helpers.CapellaProjectHelper.TriStateBoolean;
import org.polarsys.capella.vp.price.generic.IPriceVisitor;
import org.polarsys.capella.vp.price.helpers.PriceHelper;
import org.polarsys.capella.vp.price.price.Price;



public class CapellaPriceVisitor implements IPriceVisitor {
	/**
	 * Singleton Visitor object
	 */
	public static CapellaPriceVisitor INSTANCE = new CapellaPriceVisitor();
	
	/**
	 * Return a Part witch may contain wpCriteria Objects
	 * @param container : a Part or a PhysicalComponent object. 
	 * @return Part object : - If the eObject is an instance of Part then return eObject
	 * 						 - if eObject is a Physical Component then
	 * 								if it is the Physical System the 
	 * 									return it's first Abstract Typed Element
	 * 								else
	 * 									return it's first Owned Partitions 
	 */
	protected Part getCorrectPartObject(EObject container){		
		if (!(container instanceof Part || container instanceof PhysicalComponent)){
			return null;
		}

		if (container instanceof Part){
			return (Part)container;
		}else if (container instanceof PhysicalComponent){
			PhysicalComponent pc = (PhysicalComponent) container;
			if (PriceHelper.isPhysicalSystem(pc) ||
					CapellaProjectHelper.isReusableComponentsDriven(pc) == TriStateBoolean.False){
				return (Part)pc.getAbstractTypedElements().get(0);
			}
		}
		return null;
	}
	

	public Price getPriceObject(EObject eObject) {
		if (eObject instanceof Price){
			return (Price) eObject;
		}
		Part thePart = getCorrectPartObject(eObject);

		if (thePart != null){
			for (EObject eO : thePart.eContents()) {
				if (eO instanceof Price){
					return (Price)eO;
				}
			}
		}
		return null;
	}

	/**
	 * Look for Price Objects defined in a Part
	 * @param container : the Melody Advance object
	 * @return EList<wpCriteria> : all defined wpCriteria Object into the container object parameter
	 */
	public EList<Price> getPriceObjects(EObject container) {
		EList<Price> wpCriteriaObjectList = new BasicEList<Price>();

		Part thePart = getCorrectPartObject(container);
		
		if (thePart != null){
			for (EObject eO : thePart.eContents()) {
				if (eO instanceof Price){
					wpCriteriaObjectList.add((Price)eO);
				}
			}
		}
		
		return wpCriteriaObjectList;
	}
	
	/**
	 * Browser of the Melody Advance Tree.
	 * @param eObject : the Melody Advance Object to visit 
	 * @return EList<EObject> : all sub-component of the eObject parameter needed to browse the tree
	 */
	public EList<EObject> getSubComponentsObjects(EObject eObject) {
		EList<EObject> list = new BasicEList<EObject>();

		EObject type = null;
		
		EList<AbstractDeploymentLink> deployedLinkList = null;
		
		if (eObject instanceof  Part){
			type = ((Part) eObject).getAbstractType();
			deployedLinkList = ((Part) eObject).getDeploymentLinks();
			
		}else if (eObject instanceof Component) {
			type = eObject;
		}
		
		// Traitement des composants
		if (type != null && type instanceof  Component)
		{
			Component pc = (Component)type;
			for (Feature feature : pc.getOwnedFeatures()) 
			{
				if (feature instanceof Part) 
				{
					Part part = (Part)feature;
					// Traitement des Composants fils du composant
					EObject type2 = part.getType();
					if (type2 instanceof PhysicalComponent)
					{
						EList<Part> deploying = part.getDeployingParts();
						if (deploying == null || deploying.size() == 0)
							list.add(part);
					}
				}
			}
		}
		
		// Traitement des Deployements
		if (deployedLinkList != null && deployedLinkList.size() > 0)
		{
			for (AbstractDeploymentLink curDeploymentLink : deployedLinkList) 
			{
				EObject deployedComponent = curDeploymentLink.getDeployedElement();
				if (deployedComponent instanceof Part)
				{
					list.add(deployedComponent);
				}
			}
		}

		return list;
	}

}
