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

import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.skydoves.whatif.whatIfNotNullOrEmpty

object ViewBinding {
  @JvmStatic
  @BindingAdapter("loadImage")
  fun bindLoadImage(view: AppCompatImageView, url: String?) {
    Glide.with(view.context)
      .load(url)
      .into(view)
  }

  @JvmStatic
  @BindingAdapter("pagerAdapter")
  fun bindPagerAdapter(view: ViewPager2, adapter: FragmentStateAdapter) {
    view.adapter = adapter
    view.offscreenPageLimit = 3
  }

  @JvmStatic
  @BindingAdapter("gone")
  fun bindGone(view: View, shouldBeGone: Boolean?) {
    if (shouldBeGone == true) {
      view.visibility = View.GONE
    } else {
      view.visibility = View.VISIBLE
    }
  }

  @JvmStatic
  @BindingAdapter("toast")
  fun bindToast(view: View, text: String?) {
    text.whatIfNotNullOrEmpty {
      Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
    }
  }
}
