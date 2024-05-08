package com.design.appproject.ui.jiankangshuju

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
import com.design.appproject.bean.JiankangshujuItemBean
import com.blankj.utilcode.constant.TimeConstants
import com.design.appproject.databinding.JiankangshujuaddorupdateLayoutBinding
import com.design.appproject.ext.load
import android.text.InputType

/**
 * 健康数据新增或修改类
 */
@Route(path = CommonArouteApi.PATH_ACTIVITY_ADDORUPDATE_JIANKANGSHUJU)
class AddOrUpdateActivity:BaseBindingActivity<JiankangshujuaddorupdateLayoutBinding>() {

    @JvmField
    @Autowired
    var mId: Long = 0L /*id*/

    @JvmField
    @Autowired
    var mRefid: Long = 0 /*refid数据*/

    /**上传数据*/
    var mJiankangshujuItemBean = JiankangshujuItemBean()

    override fun initEvent() {
        setBarTitle("健康数据")
        setBarColor("#FFFFFF","black")
        if (mRefid>0){/*如果上一级页面传递了refid，获取改refid数据信息*/
            if (mJiankangshujuItemBean.javaClass.declaredFields.any{it.name == "refid"}){
                mJiankangshujuItemBean.javaClass.getDeclaredField("refid").also { it.isAccessible=true }.let {
                    it.set(mJiankangshujuItemBean,mRefid)
                }
            }
            if (mJiankangshujuItemBean.javaClass.declaredFields.any{it.name == "nickname"}){
                mJiankangshujuItemBean.javaClass.getDeclaredField("nickname").also { it.isAccessible=true }.let {
                    it.set(mJiankangshujuItemBean,StorageUtil.decodeString(CommonBean.USERNAME_KEY)?:"")
                }
            }
        }
        if (Utils.isLogin() && mJiankangshujuItemBean.javaClass.declaredFields.any{it.name == "userid"}){/*如果有登陆，获取登陆后保存的userid*/
            mJiankangshujuItemBean.javaClass.getDeclaredField("userid").also { it.isAccessible=true }.let {
                it.set(mJiankangshujuItemBean,Utils.getUserId())
            }
        }
        binding.initView()

    }

    fun JiankangshujuaddorupdateLayoutBinding.initView(){
            mJiankangshujuItemBean.jiluriqi = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd"))
            jiluriqiTv.text = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd"))
            val mjiluriqiPicker = DatePicker(this@AddOrUpdateActivity).apply {
                wheelLayout.setDateFormatter(BirthdayFormatter())
                wheelLayout.setRange(DateEntity.target(1923, 1, 1),DateEntity.target(2050, 12, 31), DateEntity.today())
                setOnDatePickedListener { year, month, day ->
                    jiluriqiTv.text = "$year-$month-$day"
                    mJiankangshujuItemBean.jiluriqi="$year-$month-$day"
                }
            }
            jiluriqiTv.setOnClickListener {
                mjiluriqiPicker.show()
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
                    if (mJiankangshujuItemBean.yonghuzhanghao.isNullOrEmpty()){
                        mJiankangshujuItemBean.yonghuzhanghao = it["yonghuzhanghao"]?.toString()?:""
                    }
                    binding.yonghuzhanghaoEt.keyListener = null
                    if (mJiankangshujuItemBean.yonghuming.isNullOrEmpty()){
                        mJiankangshujuItemBean.yonghuming = it["yonghuming"]?.toString()?:""
                    }
                    binding.yonghumingEt.keyListener = null
                    if (mJiankangshujuItemBean.xingbie.isNullOrEmpty()){
                        mJiankangshujuItemBean.xingbie = it["xingbie"]?.toString()?:""
                    }
                    binding.xingbieEt.keyListener = null
                    binding.setData()
                }
            }
        }

        (mId>0).yes {/*更新操作*/
            HomeRepository.info<JiankangshujuItemBean>("jiankangshuju",mId).observeKt {
                it.getOrNull()?.let {
                    mJiankangshujuItemBean = it.data
                    mJiankangshujuItemBean.id = mId
                    binding.setData()
                }
            }
        }
        binding.setData()
    }

    /**验证*/
    private fun JiankangshujuaddorupdateLayoutBinding.submit() {
        mJiankangshujuItemBean.yonghuzhanghao = yonghuzhanghaoEt.text.toString()
        mJiankangshujuItemBean.yonghuming = yonghumingEt.text.toString()
        mJiankangshujuItemBean.xingbie = xingbieEt.text.toString()
        mJiankangshujuItemBean.nianling = nianlingEt.text.toString()
        mJiankangshujuItemBean.shengao = shengaoEt.text.toString()
        mJiankangshujuItemBean.tizhong = tizhongEt.text.toString()
        mJiankangshujuItemBean.xuetang = xuetangEt.text.toString()
        mJiankangshujuItemBean.xinlv = xinlvEt.text.toString()
        mJiankangshujuItemBean.xueya = xueyaEt.text.toString()
        mJiankangshujuItemBean.danguchun = danguchunEt.text.toString()
        mJiankangshujuItemBean.guominshiwu = guominshiwuEt.text.toString()
        mJiankangshujuItemBean.jibingshi = jibingshiEt.text.toString()
        if(mJiankangshujuItemBean.yonghuzhanghao.isNullOrEmpty()){
            "用户账号不能为空".showToast()
            return
        }
        if(mJiankangshujuItemBean.yonghuming.isNullOrEmpty()){
            "用户名不能为空".showToast()
            return
        }
        if(mJiankangshujuItemBean.shengao.isNullOrEmpty()){
            "身高不能为空".showToast()
            return
        }
        if(mJiankangshujuItemBean.tizhong.isNullOrEmpty()){
            "体重不能为空".showToast()
            return
        }
        addOrUpdate()

}
    private fun addOrUpdate(){/*更新或添加*/
        if (mJiankangshujuItemBean.id>0){
            UserRepository.update("jiankangshuju",mJiankangshujuItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }else{
            HomeRepository.add<JiankangshujuItemBean>("jiankangshuju",mJiankangshujuItemBean).observeKt{
            it.getOrNull()?.let {
                "提交成功".showToast()
                finish()
            }
        }
        }
    }


    private fun JiankangshujuaddorupdateLayoutBinding.setData(){
        if (mJiankangshujuItemBean.yonghuzhanghao.isNotNullOrEmpty()){
            yonghuzhanghaoEt.setText(mJiankangshujuItemBean.yonghuzhanghao.toString())
        }
        if (mJiankangshujuItemBean.yonghuming.isNotNullOrEmpty()){
            yonghumingEt.setText(mJiankangshujuItemBean.yonghuming.toString())
        }
        if (mJiankangshujuItemBean.xingbie.isNotNullOrEmpty()){
            xingbieEt.setText(mJiankangshujuItemBean.xingbie.toString())
        }
        if (mJiankangshujuItemBean.nianling.isNotNullOrEmpty()){
            nianlingEt.setText(mJiankangshujuItemBean.nianling.toString())
        }
        if (mJiankangshujuItemBean.shengao.isNotNullOrEmpty()){
            shengaoEt.setText(mJiankangshujuItemBean.shengao.toString())
        }
        if (mJiankangshujuItemBean.tizhong.isNotNullOrEmpty()){
            tizhongEt.setText(mJiankangshujuItemBean.tizhong.toString())
        }
        if (mJiankangshujuItemBean.xuetang.isNotNullOrEmpty()){
            xuetangEt.setText(mJiankangshujuItemBean.xuetang.toString())
        }
        if (mJiankangshujuItemBean.xinlv.isNotNullOrEmpty()){
            xinlvEt.setText(mJiankangshujuItemBean.xinlv.toString())
        }
        if (mJiankangshujuItemBean.xueya.isNotNullOrEmpty()){
            xueyaEt.setText(mJiankangshujuItemBean.xueya.toString())
        }
        if (mJiankangshujuItemBean.danguchun.isNotNullOrEmpty()){
            danguchunEt.setText(mJiankangshujuItemBean.danguchun.toString())
        }
        if (mJiankangshujuItemBean.guominshiwu.isNotNullOrEmpty()){
            guominshiwuEt.setText(mJiankangshujuItemBean.guominshiwu.toString())
        }
        if (mJiankangshujuItemBean.jibingshi.isNotNullOrEmpty()){
            jibingshiEt.setText(mJiankangshujuItemBean.jibingshi.toString())
        }
    }
}