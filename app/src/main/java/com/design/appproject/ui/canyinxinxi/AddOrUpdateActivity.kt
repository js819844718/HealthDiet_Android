package com.design.appproject.ui.canyinxinxi

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
import com.design.appproject.bean.CanyinxinxiItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.databinding.CanyinxinxiaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 餐饮信息新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_CANYINXINXI)
class AddOrUpdateActivity:BaseBindingActivity<CanyinxinxiaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mCanyinxinxiItemBean = CanyinxinxiItemBean()

    override fun initEvent() {
        setBarTitle("餐饮信息")
        setBarColor("#FFFFFF","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mCanyinxinxiItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mCanyinxinxiItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mCanyinxinxiItemBean,mRefid)
                }
            }
            if (mCanyinxinxiItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mCanyinxinxiItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mCanyinxinxiItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mCanyinxinxiItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mCanyinxinxiItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mCanyinxinxiItemBean,Utils.getUserId())
            }
        }
        binding.initView()

        binding.caipinjianjieRichLayout.apply{
            actionBold.setOnClickListener {
                richEt.setBold()
            }
            actionItalic.setOnClickListener {
                richEt.setItalic()
            }
            actionStrikethrough.setOnClickListener {
                richEt.setStrikeThrough()
            }
            actionUnderline.setOnClickListener {
                richEt.setUnderline()
            }
            actionHeading1.setOnClickListener {
                richEt.setHeading(1)
            }
            actionHeading2.setOnClickListener {
                richEt.setHeading(2)
            }
            actionHeading3.setOnClickListener {
                richEt.setHeading(3)
            }
            actionHeading4.setOnClickListener {
                richEt.setHeading(4)
            }
            actionHeading5.setOnClickListener {
                richEt.setHeading(5)
            }
            actionIndent.setOnClickListener {
                richEt.setIndent()
            }
            actionOutdent.setOnClickListener {
                richEt.setOutdent()
            }
            actionAlignCenter.setOnClickListener {
                richEt.setAlignCenter()
            }
            actionAlignLeft.setOnClickListener {
                richEt.setAlignLeft()
            }
            actionAlignRight.setOnClickListener {
                richEt.setAlignRight()
            }
            actionInsertBullets.setOnClickListener {
                richEt.setBullets()
            }
            actionInsertNumbers.setOnClickListener {
                richEt.setNumbers()
            }
            actionInsertImage.setOnClickListener {
                SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                    val path = it[0]
                    UserRepository.upload(File(path),"").observeKt {
                        it.getOrNull()?.let {
                            richEt.insertImage(UrlPrefix.URL_PREFIX+"file/" + it.file, "dachshund", 320)
                        }
                    }
                }
            }
        }
    }

    fun CanyinxinxiaddorupdateLayoutBinding.initView(){
             caipintupianLl.setOnClickListener {
            SmartPictureSelector.openPicture(this@AddOrUpdateActivity) {
                val path = it[0]
                showLoading("上传中...")
                UserRepository.upload(File(path), "caipintupian").observeKt{
                    it.getOrNull()?.let {
                        caipintupianIfv.load(this@AddOrUpdateActivity, "file/"+it.file)
                        mCanyinxinxiItemBean.caipintupian = "file/" + it.file
                    }
                }
            }
        }
            yingyangjibieBs.let { spinner ->
            spinner.setOnItemSelectedListener(object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    spinner.text = content
                    mCanyinxinxiItemBean.yingyangjibie =content
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
            tuijianzhishuBs.setOptions("五星,四星,三星,二星,一星".split(","),"请选择推荐指数",
            listener = object : BottomSpinner.OnItemSelectedListener {
                override fun onItemSelected(position: Int, content: String) {
                    super.onItemSelected(position, content)
                    tuijianzhishuBs.text = content
                    mCanyinxinxiItemBean.tuijianzhishu =content
                }
            })

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
            HomeRepository.info<CanyinxinxiItemBean>("canyinxinxi",mId).observeKt {
                it.getOrNull()?.let {
                    mCanyinxinxiItemBean = it.data
                    mCanyinxinxiItemBean.id = mId
                    binding.setData()
                }
            }
        }
        mCanyinxinxiItemBean.storeupnum = 0
        binding.setData()
    }

    /**验证*/
    private fun CanyinxinxiaddorupdateLayoutBinding.submit() {
        mCanyinxinxiItemBean.caipinmingcheng = caipinmingchengEt.text.toString()
        mCanyinxinxiItemBean.caipinfenlei = caipinfenleiEt.text.toString()
        mCanyinxinxiItemBean.caipinjianjie = caipinjianjieRichLayout.richEt.html
        storeupnumEt.inputType = InputType.TYPE_CLASS_NUMBER
        mCanyinxinxiItemBean.storeupnum = storeupnumEt.text.toString().toInt()
        if(mCanyinxinxiItemBean.caipinmingcheng.isNullOrEmpty()){
            "菜品名称不能为空".showToast()
            return
        }
        if(mCanyinxinxiItemBean.caipintupian.isNullOrEmpty()){
            "菜品图片不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mCanyinxinxiItemBean.id>0){
            UserRepository.update("canyinxinxi",mCanyinxinxiItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<CanyinxinxiItemBean>("canyinxinxi",mCanyinxinxiItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun CanyinxinxiaddorupdateLayoutBinding.setData(){
        if (mCanyinxinxiItemBean.caipinmingcheng.isNotNullOrEmpty()){
            caipinmingchengEt.setText(mCanyinxinxiItemBean.caipinmingcheng.toString())
        }
        if (mCanyinxinxiItemBean.caipintupian.isNotNullOrEmpty()){
            caipintupianIfv.load(this@AddOrUpdateActivity, mCanyinxinxiItemBean.caipintupian)
        }
        if (mCanyinxinxiItemBean.caipinfenlei.isNotNullOrEmpty()){
            caipinfenleiEt.setText(mCanyinxinxiItemBean.caipinfenlei.toString())
        }
        if (mCanyinxinxiItemBean.yingyangjibie.isNotNullOrEmpty()){
            yingyangjibieBs.text =mCanyinxinxiItemBean.yingyangjibie
        }
        if (mCanyinxinxiItemBean.tuijianzhishu.isNotNullOrEmpty()){
            tuijianzhishuBs.text =mCanyinxinxiItemBean.tuijianzhishu
        }
        if (mCanyinxinxiItemBean.storeupnum>=0){
            storeupnumEt.setText(mCanyinxinxiItemBean.storeupnum.toString())
        }
        caipinjianjieRichLayout.richEt.setHtml(mCanyinxinxiItemBean.caipinjianjie)
    }
}