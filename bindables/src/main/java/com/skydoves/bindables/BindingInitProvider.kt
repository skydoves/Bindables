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

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log

/**
 * @author skydoves (Jaewoong Eum)
 *
 * A binding initialization provider for binding `@Bindable` properties when an application is started.
 * This initialization provider will be registered on the application as a content provider.
 */
class BindingInitProvider : ContentProvider() {

  override fun onCreate(): Boolean {
    val fieldSize = BindingManager.bind<BR>()
    if (fieldSize <= MIN_FIELD_SIZE) {
      Log.i(TAG, "BindingManager initialization successful, but there is no `@Bindable` field.")
    } else {
      Log.i(TAG, "BindingManager initialization successful. Size: $fieldSize")
    }
    return false
  }

  override fun getType(uri: Uri): String? = null
  override fun insert(uri: Uri, values: ContentValues?): Uri? = null
  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
  override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
  override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null

  companion object {
    private const val TAG = "BindingInitProvider"
    private const val MIN_FIELD_SIZE = 1
  }
}
