package uz.pdp.olxuz.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.olxuz.R
import uz.pdp.olxuz.database.database.AppDatabase
import uz.pdp.olxuz.databinding.ItemProductBinding
import uz.pdp.olxuz.database.entity.Product

class ProductAdapter(
    private val context: Context,
    private val list: List<Product>,
    private val listener: OnclickListener,
) : RecyclerView.Adapter<ProductAdapter.Vh>() {

    inner class Vh(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: Product, position: Int) {

            Picasso.get().load(product.image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.olx_logo)
                .into(binding.image)
            binding.productName.text = product.productName
            binding.productCity.text = product.sity
            binding.productDate.text = product.date
            binding.productPrice.text = product.salary
            binding.like.setOnClickListener {
                listener.likeOnClick(binding, product)
            }
            binding.itemProduct.setOnClickListener {
                listener.onItemClickListener(product)
            }
            val appDatabase = AppDatabase.getDatabase(context)
            if (appDatabase.productDao().getProductForLike().contains(product)) {
                binding.like.setImageResource(R.drawable.ic_liked)
            } else {
                binding.like.setImageResource(R.drawable.ic_like)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemProductBinding.inflate(LayoutInflater.from(parent.context), null, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    class MyDiffUtill : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnclickListener {
        fun onItemClickListener(product: Product)
        fun likeOnClick(itemBinding: ItemProductBinding, product: Product)
    }
}