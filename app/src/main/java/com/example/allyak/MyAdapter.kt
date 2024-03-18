package com.example.allyak

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.allyak.databinding.ItemCommunityBinding
import java.util.Locale

class MyViewHolder(val binding: ItemCommunityBinding) : RecyclerView.ViewHolder(binding.root)
class MyAdapter(val context: Context, var itemList: MutableList<ItemData>): RecyclerView.Adapter<MyViewHolder>(), Filterable {

    private var filteredItemList: List<ItemData> = itemList
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint.toString().lowercase(Locale.getDefault())
                filteredItemList = if (queryString.isEmpty()) {
                    itemList
                } else {
                    itemList.filter { it.title?.lowercase(Locale.getDefault())?.contains(queryString) == true }.toMutableList()
                }

                val filterResults = FilterResults()
                filterResults.values = filteredItemList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItemList = results?.values as List<ItemData>
                notifyDataSetChanged()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemCommunityBinding.inflate(layoutInflater))
    }
    override fun getItemCount(): Int {
        return filteredItemList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val data = filteredItemList[position]
            holder.binding.run {   //커뮤니티 뷰
                communityTitle.text = data.title
                communityTime.text = data.viewdate
                communityLike.text = data.likeCnt.toString()
                communityCmt.text = data.commentCnt.toString()
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ViewpostActivity::class.java)
                intent.putExtra("key", data.docId)
                intent.putExtra("uid", data.UID)
                intent.putExtra("title", data.title)
                intent.putExtra("content", data.content)
                intent.putExtra("date", data.date)
                intent.putExtra("viewdate", data.viewdate)
                intent.putExtra("likeCnt", data.likeCnt.toString())
                intent.putExtra("commentCnt", data.commentCnt.toString())
                context.startActivity(intent)
            }
        }

}