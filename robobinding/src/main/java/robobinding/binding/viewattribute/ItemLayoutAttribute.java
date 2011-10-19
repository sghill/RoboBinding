/**
 * Copyright 2011 Cheng Wei, Robert Taylor
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
package robobinding.binding.viewattribute;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import robobinding.beans.PresentationModelAdapter;
import robobinding.presentationmodel.DataSetAdapter;
import robobinding.value.ValueModel;
import android.content.Context;

/**
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Robert Taylor
 *
 */
public class ItemLayoutAttribute implements AdapterViewAttribute
{
	private final AdapterViewAttribute itemLayoutAttribute;
	
	public ItemLayoutAttribute(String itemLayoutAttributeValue)
	{
		if (itemLayoutAttributeValue.startsWith("@"))
			itemLayoutAttribute = new StaticItemLayoutAttribute(itemLayoutAttributeValue);
		else
			itemLayoutAttribute = new DynamicItemLayoutAttribute(itemLayoutAttributeValue);
	}

	@Override
	public void bind(DataSetAdapter<?> dataSetAdapter, PresentationModelAdapter presentationModelAdapter, Context context)
	{
		itemLayoutAttribute.bind(dataSetAdapter, presentationModelAdapter, context);			
	}
	
	private static class DynamicItemLayoutAttribute extends AbstractReadOnlyPropertyViewAttribute<Integer> implements AdapterViewAttribute
	{
		public DynamicItemLayoutAttribute(String attributeValue)
		{
			super(attributeValue);
		}

		private DataSetAdapter<?> dataSetAdapter;
		
		@Override
		public void bind(DataSetAdapter<?> dataSetAdapter, PresentationModelAdapter presentationModelAdapter, Context context)
		{
			this.dataSetAdapter = dataSetAdapter;
			super.bind(presentationModelAdapter, context);
		}

		@Override
		protected void initializeView(ValueModel<Integer> valueModel)
		{
			dataSetAdapter.setItemLayoutId(valueModel.getValue());
		}

		@Override
		protected void observeChangesOnTheValueModel(final ValueModel<Integer> valueModel)
		{
			valueModel.addValueChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt)
				{
					dataSetAdapter.setItemLayoutId(valueModel.getValue());
				}
			});
		}
	}
	
	private static class StaticItemLayoutAttribute implements AdapterViewAttribute
	{
		private static final Pattern RESOURCE_NAME_PATTERN = Pattern.compile("^@((\\w+)/\\w+$)");

		private String resourceName;
		private String resourceType;
		
		public StaticItemLayoutAttribute(String itemLayoutAttributeValue)
		{
			determineResourceNameAndType(itemLayoutAttributeValue);
		}

		private void determineResourceNameAndType(String itemLayoutAttributeValue)
		{
			Matcher matcher = RESOURCE_NAME_PATTERN.matcher(itemLayoutAttributeValue);
			matcher.find();
			if (!matcher.matches() || matcher.groupCount() != 2)
				throw new RuntimeException("Invalid itemLayout resource syntax: " + itemLayoutAttributeValue);
			
			resourceName = matcher.group(1);
			resourceType = matcher.group(2);
		}

		@Override
		public void bind(DataSetAdapter<?> dataSetAdapter, PresentationModelAdapter presentationModelAdapter, Context context)
		{
			int itemLayoutId = context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
			dataSetAdapter.setItemLayoutId(itemLayoutId);
		}
	}
}