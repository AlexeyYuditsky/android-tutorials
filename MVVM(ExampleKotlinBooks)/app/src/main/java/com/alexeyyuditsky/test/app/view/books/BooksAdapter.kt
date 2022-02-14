package com.alexeyyuditsky.test.app.view.books

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexeyyuditsky.test.app.model.Book
import com.alexeyyuditsky.test.databinding.ItemBookBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class BooksAdapter(
    private val listener: Listener,
) : RecyclerView.Adapter<BooksAdapter.MyViewHolder>(), View.OnClickListener,
    View.OnLongClickListener {

    var items: List<Book> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.tag = item
        holder.binding.apply {
            Glide.with(bookImageView.context)
                .load(item.image)
                .into(bookImageView)
            nameTextView.text = item.name
            authorTextView.text = item.authors
            yearTextView.text = item.year
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onClick(v: View) {
        val item = v.tag as Book
        listener.onBookChosen(item)
    }

    override fun onLongClick(v: View): Boolean {
        val item = v.tag as Book
        Snackbar.make(v, item.description, Snackbar.LENGTH_LONG).show()
        return true
    }

    class MyViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    interface Listener {
        fun onBookChosen(book: Book)
    }

}