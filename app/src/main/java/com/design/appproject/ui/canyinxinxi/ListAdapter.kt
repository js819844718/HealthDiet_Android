package com.design.appproject.ui.canyinxinxi
import com.union.union_basic.ext.otherwise
import com.union.union_basic.ext.yes
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.design.appproject.R
import com.design.appproject.bean.CanyinxinxiItemBean
import com.design.appproject.widget.LoadMoreAdapter
import com.design.appproject.ext.load
import com.design.appproject.utils.Utils

/**
 * 餐饮信息适配器列表
 */
class ListAdapter : LoadMoreAdapter<CanyinxinxiItemBean>(R.layout.canyinxinxi_list_item_layout) {

    var mIsBack = false/*是否后台进入*/
    override fun convert(holder: BaseViewHolder, item: CanyinxinxiItemBean) {
        holder.setText(R.id.caipinmingcheng_tv,"菜品名称:"+ item.caipinmingcheng.toString())
        val img = item.caipintupian.split(",")[0]
        holder.getView<ImageView>(R.id.picture_iv).load(context,img, needPrefix = !img.startsWith("http"))
        holder.setText(R.id.yingyangjibie_tv, item.yingyangjibie.toString())
        mIsBack.yes {
            holder.setGone(R.id.edit_fl,!Utils.isAuthBack("canyinxinxi","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthBack("canyinxinxi","删除"))
        }.otherwise {
            holder.setGone(R.id.edit_fl,!Utils.isAuthFront("canyinxinxi","修改"))
            holder.setGone(R.id.delete_fl,!Utils.isAuthFront("canyinxinxi","删除"))
        }
    }
}