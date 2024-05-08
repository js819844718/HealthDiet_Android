package com.design.appproject.ui.canyinyingyang

import android.Manifest
import com.union.union_basic.permission.PermissionUtil
import com.design.appproject.ext.UrlPrefix
import androidx.core.widget.addTextChangedListener
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.core.view.children
import com.design.appproject.utils.Utils
import com.design.appproject.bean.BaiKeBean
import androidx.core.app.ActivityCompat.startActivityForResult
import com.blankj.utilcode.util.UriUtils
import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.internal.LinkedTreeMap
import com.union.union_basic.ext.*
import com.blankj.utilcode.util.RegexUtils
import com.union.union_basic.utils.StorageUtil
import com.github.gzuliyujiang.wheelpicker.DatimePicker
import com.design.appproject.widget.BottomSpinner
import com.design.appproject.base.CommonBean
import com.blankj.utilcode.util.TimeUtils
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity
import com.github.gzuliyujiang.wheelpicker.impl.BirthdayFormatter
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter
import java.text.SimpleDateFormat
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.logic.repository.UserRepository
import com.union.union_basic.image.selector.SmartPictureSelector
import java.io.File
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.design.appproject.base.BaseBindingActivity
import com.design.appproject.base.CommonArouteApi
import com.design.appproject.bean.CanyinyingyangItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.databinding.CanyinyingyangaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 餐饮营养新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_CANYINYINGYANG)
class AddOrUpdateActivity:BaseBindingActivity<CanyinyingyangaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mCanyinyingyangItemBean = CanyinyingyangItemBean()

    override fun initEvent() {
        setBarTitle("餐饮营养")
        setBarColor("#FFFFFF","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mCanyinyingyangItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mCanyinyingyangItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mCanyinyingyangItemBean,mRefid)
                }
            }
            if (mCanyinyingyangItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mCanyinyingyangItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mCanyinyingyangItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mCanyinyingyangItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mCanyinyingyangItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mCanyinyingyangItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun CanyinyingyangaddorupdateLayoutBinding.initView(){
            caipinmingchengBs.let { spinner ->
            spinner.setOnClickListener {
                spinner.options.isNullOrEmpty().yes {
                    UserRepository.option("canyinxinxi", "caipinmingcheng", null, null,"",false).observeKt{
                        it.getOrNull()?.let {
                            spinner.setOptions(it.data, "请选择菜品名称", false)
                            spinner.dialogShow()
                        }
                    }
                }.otherwise {
                    spinner.dialogShow()
                }
            }
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mCanyinyingyangItemBean.caipinmingcheng =content
                    UserRepository.canyinxinxifollow("canyinxinxi", "caipinmingcheng", content).observeKt{
                        it.getOrNull()?.let {
                            caipinfenleiEt.setText(it.data.caipinfenlei.toString())
                            caipinfenleiEt.isFocusable = false
                            mCanyinyingyangItemBean.caipinfenlei = it.data.caipinfenlei
                        }
                    }
                }
            })
        }
             tupianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "tupian").observeKt{
                    it.getOrNull()?.let {
                        tupianIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mCanyinyingyangItemBean.tupian = "file/" + it.file
                    }
                }
            }
        }
            yingyangjibieBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mCanyinyingyangItemBean.yingyangjibie =content
                }
            })
            spinner.setOnClickListener {
                spinner.options.isNullOrEmpty().yes {
                    UserRepository.option("yingyangjibie", "yingyangjibie", "",null,"",false).observeKt{
                        it.getOrNull()?.let {
                            spinner.setOptions(it.data, "请选择营养级别", false)
                            spinner.dialogShow()
                        }
                    }
                }.otherwise {
                    spinner.dialogShow()
                }
            }
        }

            submitBtn.setOnClickListener{/*提交*/
                submit()
            }
            setData()
    }

    lateinit var mUserBean:LinkedTreeMap<String, Any>/*当前用户数据*/

    override fun initData() {
        super.initData()
        UserRepository.session<Any>().observeKt {
            it.getOrNull()?.let {
                it.data.toConversion<LinkedTreeMap<String, Any>>()?.let {
                    mUserBean = it
                    it["touxiang"]?.let { it1 -> StorageUtil.encode(CommonBean.HEAD_URL_KEY, it1) }
                    /**ss读取*/
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<CanyinyingyangItemBean>("canyinyingyang",mId).observeKt {
                it.getOrNull()?.let {
                    mCanyinyingyangItemBean = it.data
                    mCanyinyingyangItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun CanyinyingyangaddorupdateLayoutBinding.submit() {
        mCanyinyingyangItemBean.caipinfenlei = caipinfenleiEt.text.toString()
        kaluliEt.inputType = InputType.TYPE_CLASS_NUMBER
        mCanyinyingyangItemBean.kaluli = kaluliEt.text.toString().toDoubleOrNull()?:0.0
        tanshuiEt.inputType = InputType.TYPE_CLASS_NUMBER
        mCanyinyingyangItemBean.tanshui = tanshuiEt.text.toString().toDoubleOrNull()?:0.0
        danbaizhiEt.inputType = InputType.TYPE_CLASS_NUMBER
        mCanyinyingyangItemBean.danbaizhi = danbaizhiEt.text.toString().toDoubleOrNull()?:0.0
        zhifangEt.inputType = InputType.TYPE_CLASS_NUMBER
        mCanyinyingyangItemBean.zhifang = zhifangEt.text.toString().toDoubleOrNull()?:0.0
        yingyangsuEt.inputType = InputType.TYPE_CLASS_NUMBER
        mCanyinyingyangItemBean.yingyangsu = yingyangsuEt.text.toString().toDoubleOrNull()?:0.0
        if(mCanyinyingyangItemBean.caipinmingcheng.isNullOrEmpty()){
            "菜品名称不能为空".showToast()
            return
        }
        if(mCanyinyingyangItemBean.caipinfenlei.isNullOrEmpty()){
            "菜品分类不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mCanyinyingyangItemBean.id>0){
            UserRepository.update("canyinyingyang",mCanyinyingyangItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<CanyinyingyangItemBean>("canyinyingyang",mCanyinyingyangItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun CanyinyingyangaddorupdateLayoutBinding.setData(){
        if (mCanyinyingyangItemBean.caipinmingcheng.isNotNullOrEmpty()){
            caipinmingchengBs.text =mCanyinyingyangItemBean.caipinmingcheng
        }
        if (mCanyinyingyangItemBean.caipinfenlei.isNotNullOrEmpty()){
            caipinfenleiEt.setText(mCanyinyingyangItemBean.caipinfenlei.toString())
        }
        if (mCanyinyingyangItemBean.tupian.isNotNullOrEmpty()){
            tupianIfv.load(this@AddOrUpdateActivity, mCanyinyingyangItemBean.tupian)
        }
        if (mCanyinyingyangItemBean.kaluli>=0){
            kaluliEt.setText(mCanyinyingyangItemBean.kaluli.toString())
        }
        if (mCanyinyingyangItemBean.tanshui>=0){
            tanshuiEt.setText(mCanyinyingyangItemBean.tanshui.toString())
        }
        if (mCanyinyingyangItemBean.danbaizhi>=0){
            danbaizhiEt.setText(mCanyinyingyangItemBean.danbaizhi.toString())
        }
        if (mCanyinyingyangItemBean.zhifang>=0){
            zhifangEt.setText(mCanyinyingyangItemBean.zhifang.toString())
        }
        if (mCanyinyingyangItemBean.yingyangsu>=0){
            yingyangsuEt.setText(mCanyinyingyangItemBean.yingyangsu.toString())
        }
        if (mCanyinyingyangItemBean.yingyangjibie.isNotNullOrEmpty()){
            yingyangjibieBs.text =mCanyinyingyangItemBean.yingyangjibie
        }
    }
}