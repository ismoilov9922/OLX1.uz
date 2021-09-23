package uz.pdp.olxuz.ui.addproduct

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import uz.pdp.olxuz.MainActivity
import uz.pdp.olxuz.adapter.CategoryAdapter
import uz.pdp.olxuz.adapter.CategoryDialogAdapter
import uz.pdp.olxuz.databinding.DiaologProductTypeBinding
import uz.pdp.olxuz.databinding.FragmentAddProductBinding
import uz.pdp.olxuz.models.Category
import uz.pdp.olxuz.utils.LoadData

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddProductFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var firestore: FirebaseStorage
    private lateinit var reference: StorageReference
    private var imgUrl: String = ""
    private var categoryType = "all"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        firestore = FirebaseStorage.getInstance()
        reference = firestore.getReference("images")
        binding.backHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.productImage.setOnClickListener {
            Dexter.withContext(binding.root.context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        getImageContent.launch("image/*")
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?,
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).check()
        }
        binding.categoryType.setOnClickListener {
            val alertDialog = AlertDialog.Builder(binding.root.context).create()
            val dialogBinding = DiaologProductTypeBinding.inflate(layoutInflater)
            alertDialog.setView(dialogBinding.root)
            val productTypeList = LoadData.loadCategory() as ArrayList<Category>
            dialogBinding.close.setOnClickListener {
                alertDialog.dismiss()
            }
            val categoryAdapter = CategoryDialogAdapter(productTypeList,
                object : CategoryDialogAdapter.OnItemClickListener {
                    override fun onItemClick(category: Category) {
                        categoryType = category.type
                        alertDialog.dismiss()
                    }
                })
            dialogBinding.rvType.adapter = categoryAdapter
            alertDialog.show()
        }
        binding.saveAnnouncement.setOnClickListener {
        }
        return binding.root
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val m = System.currentTimeMillis()
            val uploadTask = reference.child(m.toString()).putFile(uri)
            uploadTask.addOnSuccessListener {
                if (it.task.isSuccessful) {
                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { imgUri ->
                        imgUrl = imgUri.toString()
                        Picasso.get().load(imgUri).into(binding.imageProduct1)
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
            }
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