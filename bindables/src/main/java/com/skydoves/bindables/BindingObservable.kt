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

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A binding interface that can be observed and notify changed properties.
 * This interface should be used with [PropertyChangeRegistry] and [androidx.databinding.Bindable] annotation
 * that can be applied to any getter accessor method of an [androidx.databinding.Observable].
 */
interface BindingObservable : Observable {
  /**
   * Notifies a specific property has changed that matches in [PropertyChangeRegistry].
   * This function receives a [androidx.databinding.Bindable] property and if there is a change notification of any of the
   * listed properties, this value will be refreshed.
   *
   * @param property A [androidx.databinding.Bindable] property that should be changed.
   */
  fun notifyPropertyChanged(property: KProperty<*>)

  /**
   * Notifies a specific property has changed that matches in [PropertyChangeRegistry].
   * This function receives a [androidx.databinding.Bindable] function and if there is a change notification of any of the
   * listed properties, this value will be refreshed.
   *
   * @param function A [androidx.databinding.Bindable] function that should be changed.
   */
  fun notifyPropertyChanged(function: KFunction<*>)

  /**
   * Notifies a specific property has changed that matches in [PropertyChangeRegistry].
   * This function receives a data-binding id depending on its property name and if there is a change
   * notification of any of the listed properties, this value will be refreshed.
   *
   * @param bindingId A specific data-binding id (generated BR id) that should be changed.
   */
  fun notifyPropertyChanged(bindingId: Int)

  /**
   * Notifies listeners that all properties of this instance have changed.
   */
  fun notifyAllPropertiesChanged()
}
