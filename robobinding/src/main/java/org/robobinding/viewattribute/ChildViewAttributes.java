/**
 * Copyright 2013 Cheng Wei, Robert Taylor
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

import static com.google.common.collect.Maps.newLinkedHashMap;

import java.util.Map;

import org.robobinding.BindingContext;
import org.robobinding.attribute.AbstractAttribute;
import org.robobinding.attribute.EnumAttribute;
import org.robobinding.attribute.GroupAttributes;
import org.robobinding.attribute.StaticResourceAttribute;
import org.robobinding.attribute.ValueModelAttribute;

import android.view.View;

/**
 *
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
public class ChildViewAttributes<T extends View>
{
	private AbstractViewAttributeInitializer viewAttributeInitializer;
	private Map<String, ViewAttribute> childAttributeMap;
	private boolean failOnFirstBindingError;
	private GroupAttributes groupAttributes;
	
	public ChildViewAttributes(GroupAttributes groupAttributes, AbstractViewAttributeInitializer viewAttributeInitializer)
	{
		this.groupAttributes = groupAttributes;
		this.viewAttributeInitializer = viewAttributeInitializer;
		childAttributeMap = newLinkedHashMap();
		failOnFirstBindingError = false;
	}
	
	@SuppressWarnings("unchecked")
	public <ChildViewAttributeType extends ChildViewAttribute> ChildViewAttributeType add(String attributeName, ChildViewAttributeType childAttribute)
	{
		AbstractAttribute attribute = groupAttributes.attributeFor(attributeName);
		if(childAttribute instanceof ChildViewAttributeWithAttribute<?>)
		{
			((ChildViewAttributeWithAttribute<AbstractAttribute>)childAttribute).setAttribute(attribute);
		}
		childAttributeMap.put(attributeName, new ViewAttributeAdapter(childAttribute));
		return childAttribute;
	}
	
	public <PropertyViewAttributeType extends PropertyViewAttribute<T>> PropertyViewAttributeType add(
			String propertyAttribute, PropertyViewAttributeType propertyViewAttribute)
	{
		ValueModelAttribute attributeValue = groupAttributes.valueModelAttributeFor(propertyAttribute);
		viewAttributeInitializer.initializePropertyViewAttribute(propertyViewAttribute, attributeValue);
		childAttributeMap.put(propertyAttribute, propertyViewAttribute);
		return propertyViewAttribute;
	}
	
	public void failOnFirstBindingError()
	{
		failOnFirstBindingError = true;
	}
	
	public InitializedChildViewAttributes createInitializedChildViewAttributes()
	{
		return failOnFirstBindingError ? InitializedChildViewAttributes.withFailOnFirstError(childAttributeMap) :
			InitializedChildViewAttributes.withReportAllErrors(childAttributeMap);
	}

	public final boolean hasAttribute(String attributeName)
	{
		return groupAttributes.hasAttribute(attributeName);
	}

	public final ValueModelAttribute valueModelAttributeFor(String attributeName)
	{
		return groupAttributes.valueModelAttributeFor(attributeName);
	}

	public final StaticResourceAttribute staticResourceAttributeFor(String attributeName)
	{
		return groupAttributes.staticResourceAttributeFor(attributeName);
	}
	
	public final <E extends Enum<E>> EnumAttribute<E> enumAttributeFor(String attributeName)
	{
		return groupAttributes.enumAttributeFor(attributeName);
	}
	
	private static class ViewAttributeAdapter implements ViewAttribute
	{
		private ChildViewAttribute childViewAttribute;
		public ViewAttributeAdapter(ChildViewAttribute childViewAttribute)
		{
			this.childViewAttribute = childViewAttribute;
		}
		
		@Override
		public void preInitializeView(BindingContext bindingContext)
		{
		}
		
		@Override
		public void bindTo(BindingContext bindingContext)
		{
			childViewAttribute.bindTo(bindingContext);
		}
	}
}
