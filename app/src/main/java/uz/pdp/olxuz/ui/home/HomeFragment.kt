package uz.pdp.olxuz.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import uz.pdp.olxuz.R
import uz.pdp.olxuz.adapter.CategoryAdapter
import uz.pdp.olxuz.adapter.ProductAdapter
import uz.pdp.olxuz.database.database.AppDatabase
import uz.pdp.olxuz.database.entity.Product
import uz.pdp.olxuz.databinding.FragmentHomeBinding
import uz.pdp.olxuz.databinding.ItemProductBinding
import uz.pdp.olxuz.models.Category
import uz.pdp.olxuz.ui.productview.ProductViewFragment
import uz.pdp.olxuz.utils.LoadData
import uz.pdp.olxuz.utils.NetworkHelper
import uz.pdp.olxuz.utils.Resource
import uz.pdp.olxuz.utils.Status

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "AAA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentHomeBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var list: ArrayList<Product>
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var categoryList: ArrayList<Category>
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var appDatabase: AppDatabase
    private var type = "all"
    private var isLike = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.searchHome.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        firebaseFirestore = FirebaseFirestore.getInstance()
        appDatabase = AppDatabase.getDatabase(requireContext())
        list = ArrayList()
        categoryList = LoadData.loadCategory() as ArrayList<Category>
        categoryAdapter = CategoryAdapter(categoryList)
        binding.rvCategory.adapter = categoryAdapter
        loadProduct(type).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorImage.visibility = View.INVISIBLE
                    binding.rvList.visibility = View.INVISIBLE
                }
                Status.ERROR -> {
                    binding.rvList.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.errorImage.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.progressBar.visibility = View.GONE
                        binding.errorImage.visibility = View.GONE
                        binding.rvList.visibility = View.VISIBLE
                        productAdapter =
                            ProductAdapter(requireContext(),
                                it.data,
                                object : ProductAdapter.OnclickListener {
                                    override fun onItemClickListener(product: Product) {
                                        val bundle = Bundle()
                                        bundle.putString("key", product.id)
                                        findNavController().navigate(R.id.productViewFragment,
                                            bundle)
                                    }

                                    override fun likeOnClick(
                                        itemBinding: ItemProductBinding,
                                        product: Product,
                                    ) {
                                        if (appDatabase.productDao().getProductForLike()
                                                .contains(product)
                                        ) {
                                            itemBinding.like.setImageResource(R.drawable.ic_like)
                                            appDatabase.productDao().deleteProduct(product)

                                        } else {
                                            appDatabase.productDao().insertProduct(product)
                                            itemBinding.like.setImageResource(R.drawable.ic_liked)
                                        }
                                    }
                                })
                        binding.rvList.adapter = productAdapter
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.errorImage.visibility = View.VISIBLE
                        binding.rvList.visibility = View.INVISIBLE
                    }
                }
            }
        })
        binding.refresh.setOnRefreshListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment)
            Handler().postDelayed(Runnable {
                binding.refresh.isRefreshing = false
            }, 1500)
        }
        return binding.root
    }

    private fun loadProduct(type: String): MutableLiveData<Resource<List<Product>>> {
        val productList = MutableLiveData<Resource<List<Product>>>()
        list.clear()
        if (NetworkHelper(binding.root.context).isConnected()) {
            productList.postValue(Resource.loading(null))
            LoadData.loadCategory().forEach {
                firebaseFirestore.collection(it.type).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if (document != null) {
                                val product = document.toObject(Product::class.java)
                                list.add(product)
                            }
                        }
                        productList.postValue(Resource.success(list))
                    }.addOnFailureListener {
                        productList.postValue(Resource.error("Error!!!", null))
                    }
            }
        } else {
            productList.postValue(Resource.error("Error!!!", null))
        }
        return productList
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }


}