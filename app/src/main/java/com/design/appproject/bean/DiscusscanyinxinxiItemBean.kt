package com.design.appproject.bean

/**
 * 餐饮信息评论表实体类
 */
data class DiscusscanyinxinxiItemBean(
    var id:Long=0L,
    var refid:Long=0,
    var userid:Long=0,
    var avatarurl:String="",
    var nickname:String="",
    var content:String="",
    var reply:String="",
    var addtime:String?=null,
)