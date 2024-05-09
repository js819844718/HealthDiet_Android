package com.design.appproject.logic.viewmodel.canyinxinxi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.design.appproject.bean.CanyinxinxiItemBean
import com.design.appproject.logic.repository.HomeRepository
import java.io.File
/**餐饮信息viewmodel*/
class ListModel : ViewModel() {

    private val pageData = MutableLiveData<List<Any>>()

    val pageLiveData = Transformations.switchMap(pageData) { request ->
        pageData.value?.let {
            HomeRepository.page<CanyinxinxiItemBean>(it[0] as String, it[1] as Map<String, String>)
        }
    }

    fun page(tableName: String, map: Map<String,String>) {
        pageData.value = listOf(tableName, map)
    }


    private val listData = MutableLiveData<List<Any>>()

    val listLiveData = Transformations.switchMap(listData) { request ->
        listData.value?.let {
            HomeRepository.list<CanyinxinxiItemBean>(it[0] as String, it[1] as Map<String, String>)
        }
    }

    fun list(tableName: String, map: Map<String,String>) {
        listData.value = listOf(tableName, map)
    }
}