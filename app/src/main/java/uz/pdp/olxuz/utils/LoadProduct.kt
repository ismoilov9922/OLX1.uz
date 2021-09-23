package uz.pdp.olxuz.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import uz.pdp.olxuz.database.entity.Product

class LoadProduct(val context: Context) {

    fun permissionCall(number: String) {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SEND_SMS)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data =
                        Uri.parse("tel:$number")
                    context.startActivity(callIntent)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }

    fun loadProduct(type: String): MutableLiveData<Resource<List<Product>>> {
        val productList = MutableLiveData<Resource<List<Product>>>()
        if (NetworkHelper(context).isConnected()) {
            val list = ArrayList<Product>()
            productList.postValue(Resource.loading(null))
            FirebaseFirestore.getInstance().collection(type).get()
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
        } else {
            productList.postValue(Resource.error("Error!!!", null))
        }
        return productList
    }
}