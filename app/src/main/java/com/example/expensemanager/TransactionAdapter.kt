package com.example.expensemanager

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat

class TransactionAdapter(options: FirestoreRecyclerOptions<Transaction>,private val listener:onItemCliked): FirestoreRecyclerAdapter<Transaction, TransactionViewHodler>(
    options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHodler {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.transaction_item,parent,false)
        val viewHolder=TransactionViewHodler(view)
        viewHolder.itemView.setOnClickListener(){
            listener.onItemClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: TransactionViewHodler,
        position: Int,
        model: Transaction,
    ) {

        holder.title.text=model.title
        holder.amount.text= "Rs"+model.amount.toString()

        if(model.type == "Income"){
            holder.icon.setBackgroundResource(R.drawable.icon)
        }
        else{
            holder.icon.setBackgroundResource(R.drawable.icon_red)
        }

        holder.categoryTitle.text=model.category

        when(model.category){
            "Salary" ->{
                holder.category.setImageResource(R.drawable.payment)
            }
            "Food" ->{
                holder.category.setImageResource(R.drawable.dinner)
            }
            "Entertainment" ->{
                holder.category.setImageResource(R.drawable.film)
            }
            "Insurance" ->{
                holder.category.setImageResource(R.drawable.insurance)
            }
            "Travel" ->{
                holder.category.setImageResource(R.drawable.plane)

            }
            "Medicines" ->{
                holder.category.setImageResource(R.drawable.pill)
            }
            "Personal" ->{
                holder.category.setImageResource(R.drawable.user)
            }
            "Housing" ->{
                holder.category.setImageResource(R.drawable.home)
            }
            "Others" ->{
                holder.category.setImageResource(R.drawable.more)
            }

        }


        holder.date.text= model.date
    }
}

class TransactionViewHodler(itemView: View):RecyclerView.ViewHolder(itemView){
    val cardview:CardView=itemView.findViewById(R.id.itemCardView)
    val title:TextView=itemView.findViewById(R.id.title)
    val amount:TextView=itemView.findViewById(R.id.amount)
    val icon:View=itemView.findViewById(R.id.icon)
    val date:TextView=itemView.findViewById(R.id.date)
    val categoryTitle:TextView=itemView.findViewById(R.id.category)
    val category: ImageView =itemView.findViewById(R.id.category_icon)

}

interface onItemCliked{
    fun onItemClicked(id:String)
}

