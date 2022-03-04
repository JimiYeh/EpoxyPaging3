package com.example.epoxypaging3.core.searchuser

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.example.epoxypaging3.model.SearchUserResponse

class UserAdapter : PagingDataEpoxyController<SearchUserResponse.Item>(){
  override fun buildItemModel(currentPosition: Int, item: SearchUserResponse.Item?): EpoxyModel<*> {
    return ItemUserViewModel_()
      .id(item?.id ?: -1)
      .user(item)
  }
}