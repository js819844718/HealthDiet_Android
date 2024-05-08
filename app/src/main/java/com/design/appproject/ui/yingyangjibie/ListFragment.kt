package com.design.appproject.ui.yingyangjibie

import com.design.appproject.base.NetRetrofitClient
import com.design.appproject.utils.ArouterUtils
import androidx.fragment.app.viewModels
import com.design.appproject.base.BaseBindingFragment
import com.alibaba.android.arouter.launcher.ARouter
import android.widget.*
import android.view.View
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.design.appproject.ext.load
import com.qmuiteam.qmui.layout.QMUILinearLayout
import com.blankj.utilcode.util.ColorUtils
import androidx.core.view.children
import androidx.core.view.isVisible
import com.design.appproject.R
import com.design.appproject.ui.yingyangjibie.ListFilterDialog
import android.view.ViewGroup
import android.view.Gravity
import androidx.core.view.setPadding
import androidx.core.view.setMargins
import com.design.appproject.widget.SpacesItemDecoration
import com.union.union_basic.ext.*
import net.lucode.hackware.magicindicator.MagicIndicator
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.databinding.YingyangjibiecommonListLayoutBinding
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.viewmodel.yingyangjibie.ListModel
import com.design.appproject.ui.yingyangjibie.ListAdapter
import com.design.appproject.utils.Utils
import com.google.android.flexbox.*
import com.design.appproject.logic.repository.UserRepository
import com.design.appproject.widget.MagicIndexCommonNavigator
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.blankj.utilcode.util.KeyboardUtils
import com.design.appproject.bean.YingyangjibieItemBean
import com.design.appproject.widget.MyFlexboxLayoutManager
/**
 * 营养级别列表页
 */
@Route(path = CommonArouteApi.PATH_FRAGMENT_LIST_YINGYANGJIBIE)
class ListFragment : BaseBindingFragment<YingyangjibiecommonListLayoutBinding>() {

    @JvmField
    @Autowired
    var mSearch: String = "" /*搜索*/

    @JvmField
    @Autowired
    var mIsBack: Boolean = false /*是否用户后台进入*/

    @JvmField
    @Autowired
    var mHasBack: Boolean = true /*是否有返回键*/

    private val mListAdapter by lazy {
        ListAdapter().apply {
            mIsBack = this@ListFragment.mIsBack
        }
    }

    private val mListModel by viewModels<ListModel>()

    private val params by lazy { /*请求参数*/
        mutableMapOf(
            "page" to "1",
            "limit" to "20",
        )
    }
    val queryIndex = Pair<String,String>("营养级别","yingyangjibie")/*查询索引*/

    override fun initEvent() {
        setBarTitle("营养级别",mHasBack)
        setBarColor("#FFFFFF","black")
        binding.apply {
            addBtn.isVisible = (mIsBack && Utils.isAuthBack("yingyangjibie","新增"))|| Utils.isAuthFront("yingyangjibie","新增")
            addBtn.setOnClickListener {
                ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_YINGYANGJIBIE).navigation()
            }
            initSearch()
            srv.setOnRefreshListener {
                loadData(1)
            }
            srv.mRecyclerView.layoutManager = MyFlexboxLayoutManager(requireActivity()).apply {
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.SPACE_BETWEEN
            }
            mListAdapter.addChildClickViewIds(R.id.edit_tv,R.id.delete_tv)
            mListAdapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id==R.id.edit_tv){
                  ARouter.getInstance().build(CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_YINGYANGJIBIE)
                    .withLong("mId",mListAdapter.data[position].id)
                    .navigation()
                }else if (view.id==R.id.delete_tv){
                  XPopup.Builder(requireActivity()).asConfirm("提示","是否确认删除") {
                      HomeRepository.delete<Any>("yingyangjibie",listOf(mListAdapter.data[position].id)).observeKt {
                        it.getOrNull()?.let {
                            "删除成功".showToast()
                            loadData(1)
                        }
                      }
                  }.show()
                }
            }
            srv.setAdapter(mListAdapter.apply {
                pageLoadMoreListener {
                    loadData(it)
                }
                setOnItemClickListener { adapter, view, position ->
                    ArouterUtils.startFragment(CommonArouteApi.PATH_FRAGMENT_DETAILS_YINGYANGJIBIE,map=mapOf("mId" to data[position].id,"mIsBack" to mIsBack))
                }
            })
        }
    }
    private fun YingyangjibiecommonListLayoutBinding.initSearch() {
        searchValueEt.hint = queryIndex.first
        searchValueEt.setText(mSearch)
        searchBtn.setOnClickListener {
            loadData(1)
        }
        searchValueEt.postDelayed({ KeyboardUtils.hideSoftInput(searchValueEt) }, 200)
    }



    override fun initData() {
        super.initData()
        loadData(1)
        mListModel.pageLiveData.observeKt {
            it.getOrNull()?.let {
                binding.srv.setData(it.data.list,it.data.total)
            }
        }

        mListModel.listLiveData.observeKt {
            it.getOrNull()?.let {
                binding.srv.setData(it.data.list,it.data.total)
            }
        }

    }

    private fun loadData(page:Int,searchParams:MutableMap<String,String>?=null){
        if (page==1){
            binding.srv.reload()
        }
        params.put("page",page.toString())
        binding.searchValueEt.text.toString().isNotNullOrEmpty().yes {
            params.put("yingyangjibie","%" + binding.searchValueEt.text.toString() + "%" )
        }.otherwise {
            params.remove("yingyangjibie")
        }
        val requestParams = mutableMapOf<String,String>()
        requestParams.putAll(params)
        searchParams?.let { requestParams.putAll(it) }
        mIsBack.yes {
                mListModel.page("yingyangjibie", requestParams)
        }.otherwise {
                mListModel.list("yingyangjibie", requestParams)
        }
    }
}