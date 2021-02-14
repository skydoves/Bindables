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

package com.skydoves.bindablesdemo

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.asBindingProperty
import com.skydoves.bindables.bindingProperty
import com.skydoves.bindablesdemo.recycler.Poster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BindingViewModel() {

  private val stateFlow = MutableStateFlow(listOf<Poster>())

  @get:Bindable
  val data: List<Poster>? by stateFlow.asBindingProperty()

  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set

  @get:Bindable
  var toast: String? by bindingProperty(null)
    private set

  init {
    viewModelScope.launch {
      isLoading = true

      delay(2000)

      stateFlow.emit(MockUtil.getMockPosters())

      isLoading = false

      toast = "finished loading data"
    }
  }
}
