/**
 * Copyright 2012 Cheng Wei, Robert Taylor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.robobinding.viewattribute;

import org.robobinding.BindingContext;
import org.robobinding.attribute.AbstractAttribute;
import org.robobinding.attribute.CommandAttribute;
import org.robobinding.attribute.ValueModelAttribute;

import android.view.View;

/**
 *
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Robert Taylor
 * @author Cheng Wei
 */
public class ViewAttributeInitializer
{
	private final ViewListenersInjector viewListenersInjector;
	private View view;
	
	public ViewAttributeInitializer(ViewListenersInjector viewListenersInjector)
	{
		this.viewListenersInjector = viewListenersInjector;
	}
	
	public ViewAttributeInitializer(ViewListenersInjector viewListenersInjector, View view)
	{
		this(viewListenersInjector);
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	public <ViewType extends View, PropertyViewAttributeType extends PropertyViewAttribute<ViewType>> PropertyViewAttributeType initializePropertyViewAttribute(
			PropertyViewAttributeType propertyViewAttribute, ValueModelAttribute attribute)
	{
		if(propertyViewAttribute instanceof AbstractMultiTypePropertyViewAttribute<?>)
		{
			return (PropertyViewAttributeType)initializeMultiTypePropertyViewAttribute((AbstractMultiTypePropertyViewAttribute<ViewType>)propertyViewAttribute, attribute);
		}else
		{
			return (PropertyViewAttributeType)initializePropertyViewAttribute((AbstractPropertyViewAttribute<ViewType, ?>)propertyViewAttribute, attribute);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <ViewType extends View, PropertyViewAttributeType extends AbstractPropertyViewAttribute<ViewType, ?>> PropertyViewAttributeType initializePropertyViewAttribute(
			PropertyViewAttributeType viewAttribute, ValueModelAttribute attribute)
	{
		viewAttribute.initialize(new PropertyViewAttributeConfig(getView(), attribute));
		setViewListenersIfRequired(viewAttribute);
		return viewAttribute;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <ViewType extends View, PropertyViewAttributeType extends AbstractMultiTypePropertyViewAttribute<ViewType>> PropertyViewAttributeType initializeMultiTypePropertyViewAttribute(
			PropertyViewAttributeType viewAttribute, ValueModelAttribute attribute)
	{
		viewAttribute.initialize(new MultiTypePropertyViewAttributeConfig(getView(), attribute, viewListenersInjector));
		return viewAttribute;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <ViewType extends View, CommandViewAttributeType extends AbstractCommandViewAttribute<ViewType>> CommandViewAttributeType initializeCommandViewAttribute(
			CommandViewAttributeType viewAttribute, CommandAttribute attribute)
	{
		viewAttribute.initialize(new CommandViewAttributeConfig(getView(), attribute));
		setViewListenersIfRequired(viewAttribute);
		return viewAttribute;
	}
	
	@SuppressWarnings("unchecked")
	public ViewAttribute initializeChildViewAttribute(ChildViewAttribute childViewAttribute, AbstractAttribute attribute)
	{
		if(childViewAttribute instanceof ChildViewAttributeWithAttribute<?>)
		{
			((ChildViewAttributeWithAttribute<AbstractAttribute>)childViewAttribute).setAttribute(attribute);
		}
		return new ChildViewAttributeAdapter(childViewAttribute);
	}
	
	private void setViewListenersIfRequired(ViewAttribute viewAttribute)
	{
		viewListenersInjector.injectIfRequired(viewAttribute, getView());
	}
	
	protected View getView() 
	{
		return view;
	}
	
	public void setView(View view) 
	{
		this.view = view;
	}

	private static class ChildViewAttributeAdapter implements ViewAttribute
	{
		private ChildViewAttribute childViewAttribute;
		public ChildViewAttributeAdapter(ChildViewAttribute childViewAttribute)
		{
			this.childViewAttribute = childViewAttribute;
		}
		
		@Override
		public void bindTo(BindingContext bindingContext)
		{
			childViewAttribute.bindTo(bindingContext);
		}
		
		@Override
		public void preInitializeView(BindingContext bindingContext)
		{
		}
	}
}
