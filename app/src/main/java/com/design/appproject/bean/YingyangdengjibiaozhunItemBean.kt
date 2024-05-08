package com.design.appproject.bean

/**
 * 营养等级标准实体类
 */
data class YingyangdengjibiaozhunItemBean(
    var id:Long=0L,
    var dengjimingcheng:String="",
    var biaozhunmingcheng:String="",
    var tupian:String="",
    var biaozhunshuoming:String="",
    var addtime:String?=null,
)