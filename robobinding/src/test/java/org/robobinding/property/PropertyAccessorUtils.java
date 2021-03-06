/**
 * PropertyUtilsEx.java
 * Oct 29, 2011 Copyright Cheng Wei and Robert Taylor
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
package org.robobinding.property;

import java.util.List;

import org.robobinding.internal.java_beans.PropertyDescriptor;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
public class PropertyAccessorUtils {
    private PropertyAccessorUtils() {
    }

    public static <T> PropertyAccessor<T> createPropertyAccessor(Class<?> beanClass, String propertyName) {
	PropertyDescriptor propertyDescriptor = PropertyAccessorUtils.getPropertyDescriptor(beanClass, propertyName);
	return new PropertyAccessor<T>(propertyDescriptor, beanClass);
    }

    private static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName) {
	List<PropertyDescriptor> propertyDescriptors = PropertyUtils.getPropertyDescriptors(beanClass);
	for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
	    if (propertyDescriptor.getName().equals(propertyName)) {
		return propertyDescriptor;
	    }
	}
	return null;
    }
}
