package com.example.epoxypaging3.core.searchuser

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.Glide
import com.example.epoxypaging3.databinding.ItemUserBinding
import com.example.epoxypaging3.model.SearchUserResponse

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ItemUserView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
  private val binding = ItemUserBinding.inflate(LayoutInflater.from(context), this, true)

  private var user: SearchUserResponse.Item? = null

  @ModelProp
  fun setUser(user: SearchUserResponse.Item?) {
    this.user = user
  }

  @AfterPropsSet
  fun useProps() {
    user?.let {
      Glide.with(this)
        .load(it.avatarUrl)
        .circleCrop()
        .into(binding.ivAvatar)

      binding.tvName.text = it.login
    }
  }
}