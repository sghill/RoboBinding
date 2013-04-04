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
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.robobinding.viewattribute.InitializedChildViewAttributes.withFailOnFirstError;
import static org.robobinding.viewattribute.InitializedChildViewAttributes.withReportAllErrors;
import static org.robobinding.viewattribute.InitializedChildViewAttributesTest.AttributeGroupBindingExceptionMatcher.hasChildAttributeError;

import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robobinding.BindingContext;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Robert Taylor
 */
@RunWith(MockitoJUnitRunner.class)
public class InitializedChildViewAttributesTest
{
	@Mock BindingContext bindingContext;
	@Mock ViewAttribute viewAttribute1, viewAttribute2;
	private String viewAttributeName1 = "viewAttribute1";
	private String viewAttributeName2 = "viewAttribute2";
	private Map<String, ViewAttribute> childAttributeMap;
	private InitializedChildViewAttributes childViewAttributes, childViewAttributesWithFailOnFirstError;
	@Rule public ExpectedException thrownException = ExpectedException.none();

	@Before
	public void setupChildAttributes()
	{
		childAttributeMap = newLinkedHashMap();
		childAttributeMap.put(viewAttributeName1, viewAttribute1);
		childAttributeMap.put(viewAttributeName2, viewAttribute2);
		childViewAttributes = withReportAllErrors(childAttributeMap);
		childViewAttributesWithFailOnFirstError = withFailOnFirstError(childAttributeMap);
	}

	@Test
	public void whenBinding_thenTheMethodInEachChildViewAttributeIsCalled()
	{
		childViewAttributes.bindTo(bindingContext);

		verify(viewAttribute1).bindTo(bindingContext);
		verify(viewAttribute2).bindTo(bindingContext);
	}

	@Test
	public void whenPreInitializingView_thenTheMethodOfChildPropertyViewAttributeIsCalled()
	{
		childViewAttributes.preInitializeView(bindingContext);

		verify(viewAttribute1).preInitializeView(bindingContext);
		verify(viewAttribute2).preInitializeView(bindingContext);
	}
	
	@Test
	public void whenErrorsOccurDuringBinding_thenAllErrorsAreReported()
	{
		doThrow(new RuntimeException()).when(viewAttribute1).bindTo(bindingContext);
		doThrow(new RuntimeException()).when(viewAttribute2).bindTo(bindingContext);
		
		thrownException.expect(AttributeGroupBindingException.class);
		thrownException.expect(hasChildAttributeError(viewAttributeName1));
		thrownException.expect(hasChildAttributeError(viewAttributeName2));
		
		childViewAttributes.bindTo(bindingContext);
	}

	@Test
	public void whenErrorsOccurDuringBindingWithFailOnFirstBindingError_thenOnlyTheFirstErrorIsReported()
	{
		doThrow(new RuntimeException()).when(viewAttribute1).bindTo(bindingContext);

		thrownException.expect(AttributeGroupBindingException.class);
		thrownException.expect(hasChildAttributeError(viewAttributeName1));
		thrownException.expect(not(hasChildAttributeError(viewAttributeName1)));

		childViewAttributesWithFailOnFirstError.bindTo(bindingContext);
	}
	
	@Test
	public void whenErrorsOccurDuringPreInitializingView_thenAllErrorsAreReported()
	{
		doThrow(new RuntimeException()).when(viewAttribute1).preInitializeView(bindingContext);
		doThrow(new RuntimeException()).when(viewAttribute2).preInitializeView(bindingContext);
		
		thrownException.expect(AttributeGroupBindingException.class);
		thrownException.expect(hasChildAttributeError(viewAttributeName1));
		thrownException.expect(hasChildAttributeError(viewAttributeName2));
		
		childViewAttributes.preInitializeView(bindingContext);
	}

	@Test
	public void whenErrorsOccurDuringPreInitializingViewWithFailOnFirstBindingError_thenOnlyTheFirstErrorIsReported()
	{
		doThrow(new RuntimeException()).when(viewAttribute1).preInitializeView(bindingContext);

		thrownException.expect(AttributeGroupBindingException.class);
		thrownException.expect(hasChildAttributeError(viewAttributeName1));
		thrownException.expect(not(hasChildAttributeError(viewAttributeName2)));

		childViewAttributesWithFailOnFirstError.preInitializeView(bindingContext);
	}

	static class AttributeGroupBindingExceptionMatcher extends TypeSafeMatcher<AttributeGroupBindingException>
	{
		public static AttributeGroupBindingExceptionMatcher hasChildAttributeError(String attributeName)
		{
			return new AttributeGroupBindingExceptionMatcher(attributeName);
		}

		private final String attributeName;

		private AttributeGroupBindingExceptionMatcher(String attributeName)
		{
			this.attributeName = attributeName;
		}

		@Override
		protected boolean matchesSafely(final AttributeGroupBindingException exception)
		{
			for (AttributeBindingException e : exception.getChildAttributeErrors())
				if (e.getAttribute().equals(attributeName))
					return true;

			return false;
		}

		@Override
		public void describeTo(Description description)
		{
			description.appendText("Error for attribute '").appendValue(attributeName).appendText("' was not thrown.");
		}
	}
}
