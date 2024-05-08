package com.design.appproject.bean

/**
 * 餐饮信息实体类
 */
data class CanyinxinxiItemBean(
    var id:Long=0L,
    var caipinmingcheng:String="",
    var caipintupian:String="",
    var caipinfenlei:String="",
    var yingyangjibie:String="",
    var tuijianzhishu:String="",
    var caipinjianjie:String="",
    var storeupnum:Int=0,
    var clicktime:String="",
    var addtime:String?=null,
)