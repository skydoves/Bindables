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

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A [ListAdapter] that provides a way in which UI can be notified of changes.
 * We can register an observable property using [androidx.databinding.Bindable] annotation and
 * [bindingProperty] delegates. The getter for an observable property should be annotated with [androidx.databinding.Bindable].
 */
abstract class BindingListAdapter<T, VH : RecyclerView.ViewHolder> constructor(
  val callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(callback), BindingObservable {

  /** Synchronization registry lock. */
  private val lock: Any = Any()

  /** A bindable property that indicates an item list has been submitted. */
  @get:Bindable
  var isSubmitted: Boolean = false
    private set(value) {
      if (field != value) {
        field = value
        notifyPropertyChanged(::isSubmitted)
      }
    }

  /** A callback registry for holding and notifying changes to bindable properties. */
  private var propertyCallbacks: PropertyChangeRegistry? = null

  /**
   * Adds a new [Observable.OnPropertyChangedCallback] to the property registry.
   *
   * @param callback A new [Observable.OnPropertyChangedCallback] should be added.
   */
  override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    synchronized(lock) lock@{
      val propertyCallbacks = propertyCallbacks
        ?: PropertyChangeRegistry().also { propertyCallbacks = it }
      propertyCallbacks.add(callback)
    }
  }

  /**
   * Removes an old [Observable.OnPropertyChangedCallback] from the property registry.
   *
   * @param callback An old [Observable.OnPropertyChangedCallback] should be removed.
   */
  override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    synchronized(lock) lock@{
      val propertyCallbacks = propertyCallbacks ?: return@lock
      propertyCallbacks.remove(callback)
    }
  }

  /**
   * Notifies a specific property has changed that matches in [PropertyChangeRegistry].
   * This function receives a property and if there is a change notification of any of the
   * listed properties, this value will be refreshed.
   *
   * @param property A property that should be changed.
   */
  override fun notifyPropertyChanged(property: KProperty<*>) {
    synchronized(lock) lock@{
      val propertyCallbacks = propertyCallbacks ?: return@lock
      propertyCallbacks.notifyCallbacks(this, property.bindingId(), null)
    }
  }

  /**
   * Notifies a specific property has changed that matches in [PropertyChangeRegistry].
   * This function receives a [androidx.databinding.Bindable] function and if there is a change notification of any of the
   * listed properties, this value will be refreshed.
   *
   * @param function A [androidx.databinding.Bindable] function that should be changed.
   */
  override fun notifyPropertyChanged(function: KFunction<*>) {
    synchronized(lock) lock@{
      val propertyCallbacks = propertyCallbacks ?: return@lock
      propertyCallbacks.notifyCallbacks(this, function.bindingId(), null)
    }
  }

  /**
   * Notifies a specific property has changed that matches in [PropertyChangeRegistry].
   * This function receives a data-binding id depending on its property name and if there is a change
   * notification of any of the listed properties, this value will be refreshed.
   *
   * @param bindingId A specific data-binding id (generated BR id) that should be changed.
   */
  override fun notifyPropertyChanged(bindingId: Int) {
    synchronized(lock) lock@{
      val propertyCallbacks = propertyCallbacks ?: return@lock
      propertyCallbacks.notifyCallbacks(this, bindingId, null)
    }
  }

  /**
   * Notifies listeners that all properties of this instance have changed.
   */
  override fun notifyAllPropertiesChanged() {
    synchronized(lock) lock@{
      val propertyCallbacks = propertyCallbacks ?: return@lock
      propertyCallbacks.notifyCallbacks(this, BR._all, null)
    }
  }

  /**
   * Submits a new list to be diffed, and displayed.
   */
  override fun submitList(list: List<T>?) {
    super.submitList(list)
    isSubmitted = list != null
  }

  /**
   * Set the new list to be displayed.
   */
  override fun submitList(list: List<T>?, commitCallback: Runnable?) {
    super.submitList(list, commitCallback)
    isSubmitted = list != null
  }

  /**
   * Clears all binding properties from the callback registry.
   */
  override fun clearAllProperties() {
    synchronized(lock) lock@{
      val propertyCallbacks = propertyCallbacks ?: return@lock
      propertyCallbacks.clear()
    }
  }

  /**
   * Clears all binding properties when adapter is detached from the recyclerView.
   */
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)
    clearAllProperties()
  }
}
