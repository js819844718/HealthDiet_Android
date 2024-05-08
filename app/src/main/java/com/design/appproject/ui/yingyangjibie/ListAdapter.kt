package com.design.appproject.ui.yingyangjibie
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.YingyangjibieItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 营养级别适配器列表
 */
class ListAdapter : LoadMoreAdapter<YingyangjibieItemBean>(R.layout.yingyangjibie_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: YingyangjibieItemBean) {
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("yingyangjibie","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("yingyangjibie","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("yingyangjibie","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("yingyangjibie","删除"))
        }
    }
}