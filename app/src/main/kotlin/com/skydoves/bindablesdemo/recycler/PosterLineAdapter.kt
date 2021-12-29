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

package com.skydoves.bindablesdemo.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.bindables.BindingListAdapter
import com.skydoves.bindables.binding
import com.skydoves.bindablesdemo.R
import com.skydoves.bindablesdemo.databinding.ItemPosterLineBinding

class PosterLineAdapter : BindingListAdapter<Poster, PosterLineAdapter.PosterViewHolder>(PosterDiffUtil()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
    val binding = parent.binding<ItemPosterLineBinding>(R.layout.item_poster_line)
    return PosterViewHolder(binding)
  }

  override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
    holder.binding.poster = getItem(position)
    holder.binding.executePendingBindings()
  }

  class PosterViewHolder(val binding: ItemPosterLineBinding) :
    RecyclerView.ViewHolder(binding.root)
}
