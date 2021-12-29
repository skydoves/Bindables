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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A [Flow] property for notifying a specific has changed that matches in [PropertyChangeRegistry].
 * This property only collect the flow data on the [androidx.lifecycle.viewModelScope] and notify them.
 * So this property is read-only, we can't set a value directly.
 *
 * The getter for the property that changes should be marked with [androidx.databinding.Bindable].
 *
 * @param defaultValue A default value for initializing the property value before flow emitting.
 *
 * @return A flow delegation property [FlowBindingPropertyIdWithDefaultValue].
 */
@BindingPropertyDelegate
public fun <T> Flow<T>.asBindingProperty(defaultValue: T): FlowBindingPropertyIdWithDefaultValue<T> =
  FlowBindingPropertyIdWithDefaultValue(this, defaultValue)

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A flow delegate class for collecting value and notifying changed value on a property.
 *
 * @param flow A flow for providing data.
 * @param defaultValue A default value for initializing the property value before flow emitting.
 */
public class FlowBindingPropertyIdWithDefaultValue<T> constructor(
  private val flow: Flow<T>,
  private val defaultValue: T
) {
  public operator fun provideDelegate(bindingViewModel: BindingViewModel, property: KProperty<*>): Delegate<T> {
    val bindingId = BindingManager.getBindingIdByProperty(property)
    val delegate = Delegate(defaultValue, bindingId)
    delegate.collect(flow, bindingViewModel)
    return delegate
  }

  public class Delegate<T>(private var value: T, private val bindingId: Int) {
    public fun collect(flow: Flow<T>, bindingViewModel: BindingViewModel) {
      bindingViewModel.viewModelScope.launch {
        flow.distinctUntilChanged().collect {
          value = it
          bindingViewModel.notifyPropertyChanged(bindingId)
        }
      }
    }

    public operator fun getValue(thisRef: Any, property: KProperty<*>): T = value
  }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A [Flow] property for notifying a specific has changed that matches in [PropertyChangeRegistry].
 * This property only collect the flow data on the [androidx.lifecycle.viewModelScope] and notify them.
 * So this property is read-only, we can't set a value directly.
 *
 * The getter for the property that changes should be marked with [androidx.databinding.Bindable].
 *
 * @param coroutineScope A [CoroutineScope] where the collecting should be lunched.
 * @param defaultValue A default value for initializing the property value before flow emitting.
 *
 * @return A flow delegation property [FlowBindingPropertyIdWithDefaultValue].
 */
@BindingPropertyDelegate
public fun <T> Flow<T>.asBindingProperty(coroutineScope: CoroutineScope, defaultValue: T): FlowBindingPropertyIdWithDefaultValueOnScope<T> =
  FlowBindingPropertyIdWithDefaultValueOnScope(this, coroutineScope, defaultValue)

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A flow delegate class for collecting value and notifying changed value on a property.
 *
 * @param flow A flow for providing data.
 * @param coroutineScope A [CoroutineScope] where the collecting should be lunched.
 * @param defaultValue A default value for initializing the property value before flow emitting.
 */
public class FlowBindingPropertyIdWithDefaultValueOnScope<T> constructor(
  private val flow: Flow<T>,
  private val coroutineScope: CoroutineScope,
  private val defaultValue: T
) {
  public operator fun provideDelegate(bindingObservable: BindingObservable, property: KProperty<*>): Delegate<T> {
    val bindingId = BindingManager.getBindingIdByProperty(property)
    val delegate = Delegate(defaultValue, coroutineScope, bindingId)
    delegate.collect(flow, bindingObservable)
    return delegate
  }

  public class Delegate<T>(
    private var value: T,
    private val coroutineScope: CoroutineScope,
    private val bindingId: Int
  ) {
    public fun collect(flow: Flow<T>, bindingObservable: BindingObservable) {
      coroutineScope.launch {
        flow.distinctUntilChanged().collect {
          value = it
          bindingObservable.notifyPropertyChanged(bindingId)
        }
      }
    }

    public operator fun getValue(thisRef: Any, property: KProperty<*>): T = value
  }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A [StateFlow] property for notifying a specific has changed that matches in [PropertyChangeRegistry].
 * This property only collect the flow data on the [androidx.lifecycle.viewModelScope] and notify them.
 * So this property is read-only, we can't set a value directly.
 *
 * The getter for the property that changes should be marked with [androidx.databinding.Bindable].
 *
 * @return A flow delegation property [StateFlowBindingPropertyId].
 */
@BindingPropertyDelegate
public fun <T> StateFlow<T>.asBindingProperty(): StateFlowBindingPropertyId<T> =
  StateFlowBindingPropertyId(this)

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A state flow delegate class for collecting value and notifying changed value on a property.
 *
 * @param stateFlow A state flow for providing data.
 */
public class StateFlowBindingPropertyId<T> constructor(
  private val stateFlow: StateFlow<T>,
) {

  public operator fun provideDelegate(bindingViewModel: BindingViewModel, property: KProperty<*>): Delegate<T> {
    val delegate = Delegate(stateFlow, property.bindingId)
    delegate.collect(bindingViewModel)
    return delegate
  }

  public class Delegate<T>(private val stateFlow: StateFlow<T>, private val bindingId: Int) {
    public fun collect(bindingViewModel: BindingViewModel) {
      bindingViewModel.viewModelScope.launch {
        stateFlow.collect {
          bindingViewModel.notifyPropertyChanged(bindingId)
        }
      }
    }

    public operator fun getValue(thisRef: Any, property: KProperty<*>): T = stateFlow.value
  }
}

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A [StateFlow] property for notifying a specific has changed that matches in [PropertyChangeRegistry].
 * This property only collect the flow data on the [androidx.lifecycle.viewModelScope] and notify them.
 * So this property is read-only, we can't set a value directly.
 *
 * The getter for the property that changes should be marked with [androidx.databinding.Bindable].
 *
 * @param coroutineScope A [CoroutineScope] where the collecting should be lunched.
 *
 * @return A flow delegation property [StateFlowBindingPropertyId].
 */
@BindingPropertyDelegate
public fun <T> StateFlow<T>.asBindingProperty(coroutineScope: CoroutineScope): StateFlowBindingPropertyIdOnScope<T> =
  StateFlowBindingPropertyIdOnScope(coroutineScope, this)

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A state flow delegate class for collecting value and notifying changed value on a property.
 *
 * @param coroutineScope A [CoroutineScope] where the collecting should be lunched.
 * @param stateFlow A state flow for providing data.
 */
public class StateFlowBindingPropertyIdOnScope<T> constructor(
  private val coroutineScope: CoroutineScope,
  private val stateFlow: StateFlow<T>,
) {

  public operator fun provideDelegate(bindingObservable: BindingObservable, property: KProperty<*>): Delegate<T> {
    val delegate = Delegate(stateFlow, coroutineScope, property.bindingId)
    delegate.collect(bindingObservable)
    return delegate
  }

  public class Delegate<T>(
    private val stateFlow: StateFlow<T>,
    private val coroutineScope: CoroutineScope,
    private val bindingId: Int
  ) {
    public fun collect(bindingObservable: BindingObservable) {
      coroutineScope.launch {
        stateFlow.collect {
          bindingObservable.notifyPropertyChanged(bindingId)
        }
      }
    }

    public operator fun getValue(thisRef: Any, property: KProperty<*>): T = stateFlow.value
  }
}
