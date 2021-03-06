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
package org.robobinding.viewattribute.adapterview;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robobinding.viewattribute.AbstractPropertyViewAttribute;
import org.robobinding.viewattribute.PropertyViewAttribute;
import org.robobinding.viewattribute.adapterview.SubViewVisibilityAttribute.BooleanSubViewVisibilityAttribute;
import org.robobinding.viewattribute.adapterview.SubViewVisibilityAttribute.IntegerSubViewVisibilityAttribute;

import android.view.View;

import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
@RunWith(RobolectricTestRunner.class)
public class SubViewVisibilityAttributeTest {
    private SubViewVisibilityAttribute attribute;

    @Before
    public void setUp() {
	attribute = new SubViewVisibilityAttribute(null);
    }

    @Test
    public void givenPropertyType_whenCreatePropertyViewAttribute_thenReturnExpectedInstance() {
	forPropertyType(int.class).expectAttribute(IntegerSubViewVisibilityAttribute.class);
	forPropertyType(Integer.class).expectAttribute(IntegerSubViewVisibilityAttribute.class);
	forPropertyType(Boolean.class).expectAttribute(BooleanSubViewVisibilityAttribute.class);
	forPropertyType(boolean.class).expectAttribute(BooleanSubViewVisibilityAttribute.class);
    }

    protected TypeMappingBuilder forPropertyType(Class<?> propertyType) {
	return new TypeMappingBuilder(propertyType);
    }

    protected class TypeMappingBuilder {
	private final Class<?> propertyType;

	public TypeMappingBuilder(Class<?> propertyType) {
	    this.propertyType = propertyType;
	}

	public void expectAttribute(Class<? extends PropertyViewAttribute<? extends View>> attributeClass) {
	    AbstractPropertyViewAttribute<View, ?> subAttribute = attribute.createPropertyViewAttribute(propertyType);
	    assertThat(subAttribute, instanceOf(attributeClass));
	}
    }
}