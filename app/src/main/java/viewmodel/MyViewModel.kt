package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Description:
 * @Author: zouji
 * @CreateDate: 2023/10/6 19:49
 */
class MyViewModel: ViewModel() {

    val listMutableLiveData =  MutableLiveData<ArrayList<Int>>(arrayListOf())

    /**
     * mock请求接口
     *
     */
    fun addListItem() {
        val list = arrayListOf<Int>()
//        val list = listMutableLiveData.value  //题外话：这里的value是getValue()，不是setValue不会触发liveData的onChange
        for (i in 0..19) {   //mock一次请求20条数据
            list.add(i)
        }
        listMutableLiveData.postValue(list)
    }
}