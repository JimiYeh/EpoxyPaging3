package com.example.epoxypaging3

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> Fragment.viewBinding(
  viewBindingFactory: (View) -> T
): ReadOnlyProperty<Fragment, T> = object : ReadOnlyProperty<Fragment, T> {

  private var binding: T? = null

  init {
    viewLifecycleOwnerLiveData.observe(this@viewBinding) { viewLifecycleOwner ->
      viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
//          (binding as? ViewDataBinding)?.unbind()  用 data binding 的話這行打開
          binding = null
        }
      })
    }
  }

  override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
    binding?.let {
      return it
    }

    val viewLifecycleOwner = try {
      thisRef.viewLifecycleOwner
    } catch (e: IllegalStateException) {
      error("Should not attempt to get bindings when Fragment views haven't been created yet. The fragment has not called onCreateView() at this point.")
    }

    return viewBindingFactory(thisRef.requireView()).also { viewBinding ->
      // data binding
//      if (viewBinding is ViewDataBinding) {
//        viewBinding.lifecycleOwner = viewLifecycleOwner
//      }
      binding = viewBinding
    }
  }
}


inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
  crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
  bindingInflater.invoke(layoutInflater)
}