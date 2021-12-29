/*
 * Designed and developed by 2021 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.bindables

import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A property for notifying a specific has changed that matches in [PropertyChangeRegistry].
 * The getter for the property that changes should be marked with [androidx.databinding.Bindable].
 *
 * @param defaultValue A default value should be initialized.
 *
 * @return A delegation property [BindingPropertyIdWithDefaultValue].
 */
@BindingPropertyDelegate
public fun <T> bindingProperty(defaultValue: T): BindingPropertyIdWithDefaultValue<T> =
  BindingPropertyIdWithDefaultValue(defaultValue)

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A delegate class for holding value and notifying changed value on a property.
 *
 * @param value A default value should be initialized.
 */
public class BindingPropertyIdWithDefaultValue<T>(
  private var value: T
) {
  public operator fun getValue(bindingObservable: BindingObservable, property: KProperty<*>): T = value

  public operator fun setValue(bindingObservable: BindingObservable, property: KProperty<*>, value: T) {
    if (this.value != value) {
      this.value = value
      bindingObservable.notifyPropertyChanged(property.bindingId)
    }
  }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A [SavedStateHandle] property for notifying a specific has changed that matches in [PropertyChangeRegistry].
 * We can set and get value that matches with [key] from the [SavedStateHandle].
 * Android associate the given value with the key. The value must have a type that could be stored in [android.os.Build].
 * The getter for the property that changes should be marked with [androidx.databinding.Bindable].
 *
 * @param key A key for finding saved value.
 *
 * @return A delegation property [SavedStateHandleBindingProperty].
 */
@BindingPropertyDelegate
public fun <T> SavedStateHandle.asBindingProperty(key: String): SavedStateHandleBindingProperty<T> =
  SavedStateHandleBindingProperty<T>(this, key)

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A delegate class for persisting key-value map and notifying changed value on a property.
 *
 * @param savedStateHandle A handle to saved state passed down to [androidx.lifecycle.ViewModel].
 * @param key A key for finding saved value.
 */
public class SavedStateHandleBindingProperty<T>(
  private val savedStateHandle: SavedStateHandle,
  private var key: String
) {
  public operator fun getValue(bindingObservable: BindingObservable, property: KProperty<*>): T? = savedStateHandle.get<T?>(key)

  public operator fun setValue(bindingObservable: BindingObservable, property: KProperty<*>, value: T?) {
    savedStateHandle.set(key, value)
    bindingObservable.notifyPropertyChanged(property.bindingId)
  }
}
