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
package org.robobinding.viewattribute.adapterview;

import org.robobinding.viewattribute.AbstractListeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
public class OnItemClickListeners extends AbstractListeners<OnItemClickListener> implements OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	for (OnItemClickListener listener : listeners) {
	    listener.onItemClick(parent, view, position, id);
	}
    }

    public static OnItemClickListeners convert(OnItemClickListener listener) {
	if (listener instanceof OnItemClickListeners) {
	    return (OnItemClickListeners) listener;
	} else {
	    OnItemClickListeners onItemClickListeners = new OnItemClickListeners();
	    onItemClickListeners.addListener(listener);
	    return onItemClickListeners;
	}
    }

}
