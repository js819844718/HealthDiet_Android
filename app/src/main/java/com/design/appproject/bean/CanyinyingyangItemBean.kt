package com.design.appproject.bean

/**
 * 餐饮营养实体类
 */
data class CanyinyingyangItemBean(
    var id:Long=0L,
    var caipinmingcheng:String="",
    var caipinfenlei:String="",
    var tupian:String="",
    var kaluli:Double=0.0,
    var tanshui:Double=0.0,
    var danbaizhi:Double=0.0,
    var zhifang:Double=0.0,
    var yingyangsu:Double=0.0,
    var yingyangjibie:String="",
    var addtime:String?=null,
)