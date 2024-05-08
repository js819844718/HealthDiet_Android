package com.design.appproject.ui.yingyangdengjibiaozhun
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.YingyangdengjibiaozhunItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 营养等级标准适配器列表
 */
class ListAdapter : LoadMoreAdapter<YingyangdengjibiaozhunItemBean>(R.layout.yingyangdengjibiaozhun_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: YingyangdengjibiaozhunItemBean) {
        holder.setText(R.id.dengjimingcheng_tv, item.dengjimingcheng.toString())
        val img = item.tupian.split(",")[0]
        holder.getView<ImageView>(R.id.picture_iv).load(context,img, needPrefix = !img.startsWith("http"))
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("yingyangdengjibiaozhun","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("yingyangdengjibiaozhun","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("yingyangdengjibiaozhun","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("yingyangdengjibiaozhun","删除"))
        }
    }
}