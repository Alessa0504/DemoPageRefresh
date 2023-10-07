package com.ypp.demopagerefresh

import adapter.MyAdapter
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.ypp.demopagerefresh.databinding.ActivityMainBinding
import viewmodel.MyViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler()
    private var mViewModel: MyViewModel? = null
    private var mAdapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mViewModel = ViewModelProvider(this)[MyViewModel::class.java]
        mAdapter = MyAdapter()
        mViewModel?.addListItem()  //先加载一次数据
        binding.mRecyclerView.adapter = mAdapter

        binding.mRefreshLayout.apply {
            setEnableRefresh(false)   //禁用header下拉刷新
            setEnableLoadMore(false)
//            setDisableContentWhenRefresh(true)
//            setRefreshHeader(ClassicsHeader(this@MainActivity))
            setRefreshFooter(ClassicsFooter(this@MainActivity))
            setFooterHeight(0f)    //footer样式不展示
        }
        binding.mRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                //mock上拉加载请求接口数据
                handler.postDelayed(
                    {  mViewModel?.addListItem() },
                    1000
                )
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
            }
        })

        // 设置RecyclerView的滚动监听器
        binding.mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager = (recyclerView.layoutManager as LinearLayoutManager)
                val lastVisibleItemPosition = manager.findLastVisibleItemPosition()
                val totalItemCount = manager.itemCount
                //离底部还有10条数据时预加载
                if ((lastVisibleItemPosition >= totalItemCount - 10) && (dy > 0)) {
                    binding.mRefreshLayout.autoLoadMore() // 自动触发上拉加载
                }
            }
        })

        //接口数据观察回调 onChange
        mViewModel?.listMutableLiveData?.observe(this) { list ->
            binding.mRefreshLayout.finishLoadMore()
            if (list.isNullOrEmpty()) {
                return@observe
            }
            binding.mRefreshLayout.setEnableLoadMore(true)
            mAdapter?.addData(list)
            mAdapter?.notifyDataSetChanged()

            if (mAdapter?.getData()?.size!! >= 200) {   //mock接口end标识，这里是到200条数据结束loadMore,即onLoadMore()不会再被触发
                binding.mRefreshLayout.finishLoadMore()
                binding.mRefreshLayout.setEnableLoadMore(false)
            }
        }
    }
}