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
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robobinding.attribute.AbstractAttribute;
import org.robobinding.attribute.ResolvedGroupAttributes;
import org.robobinding.attribute.ValueModelAttribute;

import android.view.View;

/**
 *
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class ChildViewAttributesTest
{
	@Mock ResolvedGroupAttributes resolvedGroupAttributes;
	@Mock ViewAttributeInitializer viewAttributeInitializer;
	@Mock InitializedChildViewAttributesFactory initializedChildViewAttributesFactory;
	@InjectMocks ChildViewAttributes<View> childViewAttributes;
	
	@Mock ChildViewAttribute childViewAttribute;
	@Mock PropertyViewAttribute<View> propertyViewAttribute;
	@Mock AbstractAttribute attribute;
	@Mock ValueModelAttribute valueModelAttribute;
	@Mock ViewAttribute viewAttribute;
	
	@Test
	public void byDefaultDontFailOnFirstError()
	{
		childViewAttributes.createInitializedChildViewAttributes();
		
		verify(initializedChildViewAttributesFactory).create(anyMap(), eq(false));
	}
	
	@Test
	public void whenFailOnFirstErrorSet_thenInitializeAccordingly()
	{
		childViewAttributes.failOnFirstBindingError();
		
		childViewAttributes.createInitializedChildViewAttributes();
		
		verify(initializedChildViewAttributesFactory).create(anyMap(), eq(true));
	}
	
	@Test
	public void whenAddingChildViewAttribute_thenAddToMap()
	{
		when(resolvedGroupAttributes.attributeFor("attributeName")).thenReturn(attribute);
		when(viewAttributeInitializer.initializeChildViewAttribute(childViewAttribute, attribute)).thenReturn(viewAttribute);
		childViewAttributes.add("attributeName", childViewAttribute);
		
		childViewAttributes.createInitializedChildViewAttributes();
		
		verify(initializedChildViewAttributesFactory).create(linkedHashMapWith("attributeName", viewAttribute), false);
	}
	
	@Test
	public void whenAddingPropertyViewAttribute_thenAddToMap()
	{
		when(resolvedGroupAttributes.valueModelAttributeFor("attributeName")).thenReturn(valueModelAttribute);
		when(viewAttributeInitializer.initializePropertyViewAttribute(propertyViewAttribute, valueModelAttribute)).thenReturn(propertyViewAttribute);
		childViewAttributes.add("attributeName", propertyViewAttribute);
		
		childViewAttributes.createInitializedChildViewAttributes();
		
		verify(initializedChildViewAttributesFactory).create(linkedHashMapWith("attributeName", propertyViewAttribute), false);
	}
	
	private Map<String, ViewAttribute> linkedHashMapWith(String attributeName, ViewAttribute viewAttribute)
	{
		Map<String, ViewAttribute> map = newLinkedHashMap();
		map.put("attributeName", viewAttribute);
		return map;
	}
}
