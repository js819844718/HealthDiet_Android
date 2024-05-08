package com.design.appproject.ui.discusscanyinxinxi

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
import com.design.appproject.bean.DiscusscanyinxinxiItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.databinding.DiscusscanyinxinxiaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 餐饮信息评论表新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_DISCUSSCANYINXINXI)
class AddOrUpdateActivity:BaseBindingActivity<DiscusscanyinxinxiaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mDiscusscanyinxinxiItemBean = DiscusscanyinxinxiItemBean()

    override fun initEvent() {
        setBarTitle("餐饮信息评论表")
        setBarColor("#FFFFFF","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mDiscusscanyinxinxiItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mDiscusscanyinxinxiItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mDiscusscanyinxinxiItemBean,mRefid)
                }
            }
            if (mDiscusscanyinxinxiItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mDiscusscanyinxinxiItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mDiscusscanyinxinxiItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mDiscusscanyinxinxiItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mDiscusscanyinxinxiItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mDiscusscanyinxinxiItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun DiscusscanyinxinxiaddorupdateLayoutBinding.initView(){

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
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<DiscusscanyinxinxiItemBean>("discusscanyinxinxi",mId).observeKt {
                it.getOrNull()?.let {
                    mDiscusscanyinxinxiItemBean = it.data
                    mDiscusscanyinxinxiItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun DiscusscanyinxinxiaddorupdateLayoutBinding.submit() {
        mDiscusscanyinxinxiItemBean.content = contentEt.text.toString()
        mDiscusscanyinxinxiItemBean.avatarurl = StorageUtil.decodeString(CommonBean.HEAD_URL_KEY)?:""
        if(mDiscusscanyinxinxiItemBean.refid<=0){
            "关联表id不能为空".showToast()
            return
        }
        if(mDiscusscanyinxinxiItemBean.userid<=0){
            "用户id不能为空".showToast()
            return
        }
        if(mDiscusscanyinxinxiItemBean.content.isNullOrEmpty()){
            "评论内容不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mDiscusscanyinxinxiItemBean.id>0){
            UserRepository.update("discusscanyinxinxi",mDiscusscanyinxinxiItemBean).observeKt{
            it.getOrNull()?.let {
                mDiscusscanyinxinxiItemBean.avatarurl = StorageUtil.decodeString(CommonBean.HEAD_URL_KEY)?:""
                setResult(RESULT_OK)
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<DiscusscanyinxinxiItemBean>("discusscanyinxinxi",mDiscusscanyinxinxiItemBean).observeKt{
            it.getOrNull()?.let {
                mDiscusscanyinxinxiItemBean.avatarurl = StorageUtil.decodeString(CommonBean.HEAD_URL_KEY)?:""
                setResult(RESULT_OK)
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun DiscusscanyinxinxiaddorupdateLayoutBinding.setData(){
        if (mDiscusscanyinxinxiItemBean.content.isNotNullOrEmpty()){
            contentEt.setText(mDiscusscanyinxinxiItemBean.content.toString())
        }
    }
}