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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robobinding.viewattribute.RandomValues;
import org.robobinding.viewattribute.adapterview.SubViewVisibilityAttribute.BooleanSubViewVisibilityAttribute;

import android.view.View;

import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
@RunWith(RobolectricTestRunner.class)
public class BooleanSubViewVisibilityAttributeTest {
    private BooleanSubViewVisibilityAttribute attribute;
    private MockSubViewVisibility mockVisibility;

    @Before
    public void setUp() {
	mockVisibility = new MockSubViewVisibility();
	SubViewVisibilityAttribute visibilityAttribute = new SubViewVisibilityAttribute(mockVisibility);
	attribute = visibilityAttribute.new BooleanSubViewVisibilityAttribute();
    }

    @Test
    public void whenValueModelUpdated_thenVisibilityStateUpdatedAccordingly() {
	boolean newValue = RandomValues.trueOrFalse();

	attribute.valueModelUpdated(newValue);

	assertEquals(newValue ? View.VISIBLE : View.GONE, mockVisibility.state);
    }
}