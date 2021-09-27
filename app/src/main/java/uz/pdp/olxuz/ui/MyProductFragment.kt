package uz.pdp.olxuz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.pdp.olxuz.MainActivity
import uz.pdp.olxuz.R
import uz.pdp.olxuz.adapter.ProductEditAdapter
import uz.pdp.olxuz.database.entity.Product
import uz.pdp.olxuz.databinding.FragmentMyProductBinding
import uz.pdp.olxuz.sharedPreference.YourPreference
import uz.pdp.olxuz.utils.LoadProduct
import uz.pdp.olxuz.utils.Status


class MyProductFragment : Fragment() {
    lateinit var binding: FragmentMyProductBinding
    lateinit var myProductList: ArrayList<String>
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var productEditAdapter: ProductEditAdapter
    lateinit var yourPreference: YourPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyProductBinding.inflate(layoutInflater)
        myProductList = ArrayList()
        yourPreference = YourPreference.getInstance(requireContext())
        firebaseFirestore = FirebaseFirestore.getInstance()
        LoadProduct(requireContext()).loadProduct("myProduct")
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.rv.visibility = View.INVISIBLE
                    }
                    Status.ERROR -> {
                        binding.progress.visibility = View.GONE
                        binding.rv.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),
                            "Sizda Hech qanday e'lon mavjud emas!",
                            Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        val list = ArrayList<Product>()
                        if (it.data != null) {
                            it.data.forEach { product ->
                                if (product.number == yourPreference.getData("phone")) {
                                    list.add(product)
                                }
                            }
                            binding.progress.visibility = View.GONE
                            binding.rv.visibility = View.VISIBLE
                            productEditAdapter =
                                ProductEditAdapter(requireContext(), list, object :
                                    ProductEditAdapter.OnclickListener {
                                    override fun onItemEdit(product: Product) {

                                    }

                                    override fun onItemDelete(product: Product) {
                                        firebaseFirestore.collection("myProduct")
                                            .document(product.id).delete()
                                        firebaseFirestore.collection(product.type)
                                            .document(product.id).delete()
//                                        FirebaseStorage.getInstance().getReference(product.image)
//                                            .delete()
                                        findNavController().navigate(R.id.myProductFragment)
                                    }
                                })
                            binding.rv.adapter = productEditAdapter
                        }
                    }
                }
            })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNawView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNawView()
    }


}