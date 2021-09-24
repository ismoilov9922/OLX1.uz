package uz.pdp.olxuz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.olxuz.R
import uz.pdp.olxuz.databinding.ItemCategoryBinding
import uz.pdp.olxuz.models.Category

class CategoryAdapter(private val list: List<Category>, private val listener: OnClickListener) :
    RecyclerView.Adapter<CategoryAdapter.Vh>() {
    inner class Vh(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category, position: Int) {
            Picasso.get().load(category.imageUrl)
                .placeholder(R.drawable.olx_logo)
                .error(R.drawable.olx_logo)
                .into(binding.image)
            binding.type.text = category.name
            binding.item.setOnClickListener {
                listener.onItemClickListener(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), null, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnClickListener {
        fun onItemClickListener(category: Category)
    }
}