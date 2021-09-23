package uz.pdp.olxuz.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.pdp.olxuz.database.entity.Product
import uz.pdp.olxuz.utils.Resource
import javax.inject.Inject

class ProductRepository @Inject constructor(private val firebaseStore: FirebaseFirestore) {

    private val products = MutableLiveData<Resource<List<Product>>>()
    private val TAG = "AAA"

    fun getProduct(type: String): LiveData<Resource<List<Product>>> {
        GlobalScope.launch {
            products.postValue(Resource.loading(null))
            try {
                coroutineScope {
                    val list = ArrayList<Product>()
                    FirebaseFirestore.getInstance().collection("all").get()
                        .addOnSuccessListener { result ->
                            for (it in result) {
                                if (it != null) {
                                    val product = it.toObject(Product::class.java)
                                    list.add(product)
                                }
                            }
                        }.addOnFailureListener {
                            products.postValue(Resource.error("Error!", null))
                        }
                    Log.d(TAG, "getProduct: $list")
                    products.postValue(Resource.success(list))
                }
            } catch (e: Exception) {
                products.postValue(Resource.error("Error!!!", null))
            }
        }
        return products
    }
}