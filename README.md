<h1 align="center">Bindables</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=16"><img alt="API" src="https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/skydoves/Bindables/actions"><img alt="Build Status" src="https://github.com/skydoves/Bindables/workflows/Android%20CI/badge.svg"/></a> 
  <a href="https://github.com/skydoves"><img alt="Profile" src="https://skydoves.github.io/badges/skydoves.svg"/></a>
  <a href="https://proandroiddev.com/improving-android-databinding-with-bindables-library-cf4b0874d56b"><img alt="Medium" src="https://skydoves.github.io/badges/Story-Medium.svg"/></a>
  <a href="https://skydoves.github.io/libraries/bindables/html/bindables/com.skydoves.bindables/index.html"><img implealt="Dokka" src="https://skydoves.github.io/badges/javadoc-bindables.svg"/></a>
</p>

<p align="center">
🧬 Android DataBinding kit for notifying data changes from Model layers to UI layers. <br>
You can notify data changes to UI layers without backing properties, and reactive programming models such as LiveData and StateFlow.
</p>

<p align="center">
<img src="https://user-images.githubusercontent.com/24237865/174428741-624646ef-c501-4299-b2b3-e4940504b3b8.png"/>
</p>

## UseCase
You can reference the good use cases of this library in the below repositories.
- [Pokedex](https://github.com/skydoves/pokedex) - 🗡️ Android Pokedex using Hilt, Motion, Coroutines, Flow, Jetpack (Room, ViewModel, LiveData) based on MVVM architecture.
- [DisneyMotions](https://github.com/skydoves/DisneyMotions) - 🦁 A Disney app using transformation motions based on MVVM (ViewModel, Coroutines, LiveData, Room, Repository, Koin) architecture.
- [MarvelHeroes](https://github.com/skydoves/marvelheroes) - ❤️ A sample Marvel heroes application based on MVVM (ViewModel, Coroutines, LiveData, Room, Repository, Koin)  architecture.
- [TheMovies2](https://github.com/skydoves/TheMovies2) - 🎬 A demo project using The Movie DB based on Kotlin MVVM architecture and material design & animations.

## Download
[![Maven Central](https://img.shields.io/maven-central/v/com.github.skydoves/bindables.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22bindables%22)

### Gradle
Add the dependency below to your **module**'s `build.gradle` file:
```gradle
dependencies {
    implementation "com.github.skydoves:bindables:1.1.0"
}
```

### SNAPSHOT 
[![Bindables](https://img.shields.io/static/v1?label=snapshot&message=bindables&logo=apache%20maven&color=C71A36)](https://oss.sonatype.org/content/repositories/snapshots/com/github/skydoves/bindables/) <br>
Snapshots of the current development version of Bindables are available, which track [the latest versions](https://oss.sonatype.org/content/repositories/snapshots/com/github/skydoves/bindables/).
```Gradle
repositories {
   maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

## Setup DataBinding
If you already use `DataBinding` in your project, you can skip this step. Add below on your `build.gradle` and make sure to use `DataBinding` in your project.
```gradle
plugins {
    ...
    id 'kotlin-kapt'
}

android {
  ...
  buildFeatures {
      dataBinding true
  }
}
```

## BindingActivity
`BindingActivity` is a base class for Activities that wish to bind content layout with `DataBindingUtil`. It provides a `binding` property that extends `ViewDataBinding` from abstract information. The `binding` property will be initialized lazily but ensures to be initialized before being called `super.onCreate` in Activities. So we don't need to inflate layouts, setContentView, and initialize a binding property manually.

```kotlin
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

   binding.vm = viewModel // we can access a `binding` property.

  // Base classes provide `binding` scope that has a receiver of the binding property.
  // So we don't need to use `with (binding) ...` block anymore.
   binding {
      lifecycleOwner = this@MainActivity
      adapter = PokemonAdapter()
      vm = viewModel
    }
  }
}
```

### Extending BindingActivity
If you want to extend `BindingActivity` for designing your own base class, you can extend like the below.

```kotlin
abstract class BaseBindingActivity<T : ViewDataBinding> constructor(
  @LayoutRes val contentLayoutId: Int
) : BindingActivity<T>(contentLayoutId) {
  
  // .. //  
}
```

## BindingFragment
The concept of the `BindingFragment` is not much different from the `BindingActivity`. It ensures the `binding` property to be initialized in `onCreateView`.

```kotlin
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    super.onCreateView(inflater, container, savedInstanceState) // we should call `super.onCreateView`.
    return binding {
      adapter = PosterAdapter()
      vm = viewModel
    }.root
  }
}
```

### Extending BindingFragment
If you want to extend `BindingFragment` for designing your own base class, you can extend like the below.

```kotlin
abstract class BaseBindingFragment<T : ViewDataBinding> constructor(
  @LayoutRes val contentLayoutId: Int
) : BindingFragment<T>(contentLayoutId) {
 
  // .. //
}
```

## BindingViewModel
`BindingViewModel` provides a way in which UI can be notified of changes by the Model layers.

### bindingProperty
`bindingProperty` notifies a specific has changed and it can be observed in UI layers. The getter for the property that changes should be marked with `@get:Bindable`.

```kotlin
class MainViewModel : BindingViewModel() {

  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set // we can prevent access to the setter from outsides.

  @get:Bindable
  var toastMessage: String? by bindingProperty(null) // two-way binding.

  fun fetchFromNetwork() {
    isLoading = true

    // ... //
  }
}
```
In our XML layout, the changes of properties value will be notified to DataBinding automatically whenever we change the value.
```xml
<ProgressBar
    android:id="@+id/progress"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:gone="@{!vm.loading}"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```

### notifyPropertyChanged
we can customize setters of general properties for notifying data changes to UI layers using `@get:Bindable` annotation and `notifyPropertyChanged()` in the `BindingViewModel`.

```kotlin
@get:Bindable
var message: String? = null
  set(value) {
    field = value
    // .. do something.. //
    notifyPropertyChanged(::message) // notify data changes to UI layers. (DataBinding)
  }
```

### Two-way binding
We can implement two-way binding properties using the `bindingProperty`. Here is a representative example of the two-way binding using `TextView` and `EditText`.

```kotlin
class MainViewModel : BindingViewModel() {
  // This is a two-way binding property because we don't set the setter as privately.
  @get:Bindable
  var editText: String? by bindingProperty(null)
}
```
Here is an XML layout. The text will be changed whenever the `viewModel.editText` is changed.
```xml
<androidx.appcompat.widget.AppCompatTextView
  android:id="@+id/textView"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:text="@{viewModel.editText}" />

<EditText
  android:id="@+id/editText"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content" />
```
In your Activity or Fragment, we can set the `viewModel.editText` value whenever the `EditText`'s input is changed. We can implement this another way using `inversebindingadapter`.
```kotlin
binding.editText.addTextChangedListener {
  vm.editText = it.toString()
}
```

### Binding functions
We can implement bindable functions using `@Bindable` annotation and `notifyPropertyChanged()` in the `BindingViewModel`. And the `@Bindable` annotated method's name must start with `get`.

```kotlin
class MainViewModel : BindingViewModel() {
  @Bindable
  fun getFetchedString(): String {
    return usecase.getFetchedData()
  }

  fun fetchDataAndNotifyChaged() {
    usecase.fetchDataFromNetowrk()
    notifyPropertyChanged(::getFetchedString)
  }
}
```
Whenever we call `notifyPropertyChanged(::getFetchedData)`, `getFetchedString()` will be called and the UI layer will get the updated data. 
```xml
android:text="@{viewModel.fetchedData}"
```

### Binding Flow
We can create a binding property from `Flow` using `@get:Bindable` and `asBindingProperty`. UI layers will get newly collected data from the `Flow` or `StateFlow` on the `viewModelScope`. And the property by the `Flow` must be read-only (val), because its value can be changed only by observing the changes of the `Flow`.

```kotlin
class MainViewModel : BindingViewModel() {

  private val stateFlow = MutableStateFlow(listOf<Poster>())

  @get:Bindable
  val data: List<Poster> by stateFlow.asBindingProperty()

  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set

  init {
    viewModelScope.launch {
      stateFlow.emit(getFetchedDataFromNetwork())

      // .. //
    }
  }
}
```

### Binding SavedStateHandle
We can create a binding property from `SavedStateHandle` in the `BindingViewModel` using `@get:Bindable` and `asBindingProperty(key: String)`. UI layers will get newly saved data from the `SavedStateHandle` and we can set the value into the `SavedStateHandle` when we just set a value to the property.

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle
) : BindingViewModel() {

  @get:Bindable
  var savedPage: Int? by savedStateHandle.asBindingProperty("PAGE")

  // .. //
```

## BindingRecyclerViewAdapter
We can create binding properties in the `RecyclerView.Adapter` using the `BindingRecyclerViewAdapter`. In the below example, the `isEmpty` property is observable in the XML layout. And we can notify value changes to DataBinding using `notifyPropertyChanged`.

```kotlin
class PosterAdapter : BindingRecyclerViewAdapter<PosterAdapter.PosterViewHolder>() {

  private val items = mutableListOf<Poster>()

  @get:Bindable
  val isEmpty: Boolean
    get() = items.isEmpty()

  fun addPosterList(list: List<Poster>) {
    items.clear()
    items.addAll(list)
    notifyDataSetChanged()
    notifyPropertyChanged(::isEmpty)
  }
}
```
In the below example, we can make the `placeholder` being gone when the adapter's item list is empty or loading data.

```xml
<androidx.appcompat.widget.AppCompatTextView
    android:id="@+id/placeholder"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="@string/empty"
    app:gone="@{!adapter.empty || viewModel.loading}" />
```

## BindingModel
We can use binding properties in our own classes via extending the `BindingModel`. 

```kotlin
class PosterUseCase : BindingModel() {

  @get:Bindable
  var message: String? by bindingProperty(null)
    private set

  init {
    message = getMessageFromNetwork()
  }
}
```

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/bindables/stargazers)__ for this repository. :star: <br>
And __[follow](https://github.com/skydoves)__ me for my next creations! 🤩

# License
```xml
Copyright 2021 skydoves (Jaewoong Eum)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
