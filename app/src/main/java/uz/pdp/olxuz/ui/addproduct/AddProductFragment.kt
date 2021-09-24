package uz.pdp.olxuz.ui.addproduct

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
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
import uz.pdp.olxuz.R
import uz.pdp.olxuz.adapter.CategoryDialogAdapter
import uz.pdp.olxuz.database.entity.Product
import uz.pdp.olxuz.databinding.DiaologProductTypeBinding
import uz.pdp.olxuz.databinding.FragmentAddProductBinding
import uz.pdp.olxuz.databinding.MapDialogBinding
import uz.pdp.olxuz.models.Category
import uz.pdp.olxuz.utils.LoadData
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

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
    lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var reference: StorageReference
    private var imgUrl: String = ""
    private var categoryType = "all"
    private var latitude = 41.326298
    private var longitude = 69.228560

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        firestore = FirebaseStorage.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
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
                        binding.typeProduct.text = category.name
                        alertDialog.dismiss()
                    }
                })
            dialogBinding.rvType.adapter = categoryAdapter
            alertDialog.show()
        }
        binding.location.setOnClickListener {
            val mapDialog = AlertDialog.Builder(requireContext())
            val dialogBinding: MapDialogBinding = MapDialogBinding.inflate(layoutInflater)
            mapDialog.setView(dialogBinding.root)
            mapDialog.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(requireContext(), "Siz turgan joy saqlandi!", Toast.LENGTH_SHORT)
                    .show()
                mapDialog.create().dismiss()
            })
            mapDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(requireContext(), "Siz turgan joy saqlanmadi", Toast.LENGTH_SHORT)
                    .show()
                mapDialog.create().dismiss()
            })
            mapDialog.show()
        }
        binding.saveAnnouncement.setOnClickListener {
            val productName = binding.title.text.toString().trim()
            val description = binding.description.text.toString().trim()
            val username = binding.username.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val telnumber = binding.telNumber.text.toString().trim()
            val salary = binding.productSalary.text.toString().trim()
            val date = Date()
            val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd")
            val dateTime = simpleDataFormat.format(date).trim()
            val time = System.currentTimeMillis()
            Log.d("AAA", "onCreateView: $productName")
            if (imgUrl.isNotEmpty() && productName.isNotEmpty() && description.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && telnumber.isNotEmpty() && salary.isNotEmpty()) {
                val product =
                    Product(time.toString(),
                        description,
                        email,
                        imgUrl,
                        "false",
                        "0",
                        latitude.toString(),
                        longitude.toString(),
                        0,
                        telnumber,
                        productName,
                        salary,
                        categoryType,
                        username,
                        time.toInt(),
                        "Toshkent",
                        dateTime)
                firebaseFirestore.collection(categoryType)
                    .add(product)
                    .addOnSuccessListener {
                        Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.homeFragment)
                    }.addOnFailureListener {
                        Toast.makeText(binding.root.context,
                            "I'm sorry do not success!!!",
                            Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(),
                    "Malumotlar to'liq to'ldirilmagan!!!",
                    Toast.LENGTH_SHORT).show()
            }
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