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
package org.robobinding;

import org.robobinding.property.ValueModel;

import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
public class ViewBinderWrapper
{
	private ViewBinder viewBinder;
	private BindingContext bindingContext;

	public ViewBinderWrapper(ViewBinder viewBinder, BindingContext bindingContext)
	{
		this.viewBinder = viewBinder;
		this.bindingContext = bindingContext;
	}

	public void attachToRoot(ViewGroup parentView)
	{
		viewBinder.attachToRoot(parentView);
	}

	public View inflateAndBind(int layoutId, Object presentationModel)
	{
		return viewBinder.inflateAndBind(layoutId, presentationModel);
	}

	public View inflateAndBind(int layoutId, String presentationModelPropertyName)
	{
		ValueModel<Object> valueModel = bindingContext.getPropertyValueModel(presentationModelPropertyName);
		Object presentationModel = valueModel.getValue();
		return viewBinder.inflateAndBind(layoutId, presentationModel);
	}

	public View inflate(int layoutId)
	{
		return viewBinder.inflate(layoutId);
	}

}
