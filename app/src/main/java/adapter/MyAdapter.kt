package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ypp.demopagerefresh.R

/**
 * @Description:
 * @Author: zouji
 * @CreateDate: 2023/10/6 20:11
 */
class MyAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: MutableList<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.tv_title)
        textView.text = "" + position
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(data: MutableList<Int>) {
        this.data.addAll(data)
    }

    fun getData(): MutableList<Int> {
        return data
    }
}