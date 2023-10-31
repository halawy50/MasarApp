package com.masar.masatapp1.UI.Student.MainPage_Student.All_Book_Student.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.masar.masatapp1.Data.BookInfo_Data
import com.masar.masatapp1.R
import com.masar.masatapp1.databinding.ItembookBinding
import com.masar.masatapp1.UI.Student.stateData.SateData.bookInfo

class Adapter_AllBook(var list_Book: List<BookInfo_Data>, var context: Context) :
    RecyclerView.Adapter<Adapter_AllBook.Adapter_Holder>() {

    inner class Adapter_Holder(var binding: ItembookBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_Holder {
       return Adapter_Holder(ItembookBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list_Book.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Adapter_Holder, position: Int) {
        var book = list_Book[position]
        holder.binding.apply {
            counter.text = "${book.counter}"
            subscribe.text = "${book.subscribe}"
            goBack.text = "${book.go_back}"

            if (book.stateBook==1){
                stateOrder.setTextColor(Color.parseColor("#FF7A00"))
                stateOrder.text = "في الانتظار"
            }
            else if (book.stateBook==0){
                stateOrder.text = "طلب مرفوض"
            }
            else if (book.stateBook==2){
                stateOrder.text = "يعمل حاليا"
            }
            else if (book.stateBook==3){
                stateOrder.text = "انتهت الرحلة"
            }
        }
        holder.itemView.setOnClickListener {
            bookInfo = book
            it.findNavController().navigate(R.id.action_all_Book_Student_Fragment_to_show_Book_Student_Fragment)
        }
    }


}
