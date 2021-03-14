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

package com.skydoves.bindablesdemo.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.bindablesdemo.recycler.Poster
import com.skydoves.whatif.whatIfNotNullAs

object RecyclerViewBinding {
  @JvmStatic
  @BindingAdapter("submitList")
  fun bindAdapterPosterList(view: RecyclerView, itemList: List<Poster>?) {
    view.adapter.whatIfNotNullAs<BindingListAdapter<Any, *>> { adapter ->
      adapter.submitList(itemList)
    }
  }
}
