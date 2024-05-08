package com.design.appproject.logic.viewmodel.discusscanyinxinxi

import com.design.appproject.logic.repository.UserRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.design.appproject.logic.repository.HomeRepository
import com.design.appproject.bean.DiscusscanyinxinxiItemBean
import com.design.appproject.bean.CommentsBean
/**
 * 餐饮信息评论表viewmodel
 * */
class DetailsViewModel : ViewModel() {

    private val infoData = MutableLiveData<List<String>>()

    val infoLiveData = Transformations.switchMap(infoData) { request ->
        infoData.value?.let {
            HomeRepository.info<DiscusscanyinxinxiItemBean>(it[0], it[1].toLong())
        }
    }

    fun info(tableName: String, id: String) {
        infoData.value = listOf(tableName, id)
    }

    private val commentsData = MutableLiveData<List<Any>>()

    val commentsLiveData = Transformations.switchMap(commentsData) { request ->
        commentsData.value?.let {
            HomeRepository.list<CommentsBean>(it[0] as String, it[1] as Map<String, String>)
        }
    }

    fun comments(tableName: String, map: Map<String,String>) {
        commentsData.value = listOf(tableName, map)
    }
    private val updateData = MutableLiveData<List<Any>>()

    val updateLiveData = Transformations.switchMap(updateData) { request ->
        updateData.value?.let {
            UserRepository.update(it[0] as String,it[1],it[2] as String)
        }
    }

    fun update(tableName: String,body: Any,tag:String="") {
        updateData.value = listOf(tableName,body,tag)
    }

}