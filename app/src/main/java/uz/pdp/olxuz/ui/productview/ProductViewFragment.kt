package uz.pdp.olxuz.ui.productview

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.pdp.olxuz.MainActivity
import uz.pdp.olxuz.R
import uz.pdp.olxuz.adapter.ProductAdapter
import uz.pdp.olxuz.database.database.AppDatabase
import uz.pdp.olxuz.database.entity.Product
import uz.pdp.olxuz.databinding.FragmentProductViewBinding
import uz.pdp.olxuz.databinding.ImageViewBinding
import uz.pdp.olxuz.databinding.ItemProductBinding
import uz.pdp.olxuz.utils.LoadProduct
import uz.pdp.olxuz.utils.NetworkHelper
import uz.pdp.olxuz.utils.Resource
import uz.pdp.olxuz.utils.Status


private const val ARG_PARAM1 = "product"

class ProductViewFragment : Fragment(), OnMapReadyCallback {
    private var product: Product? = null
    lateinit var binding: FragmentProductViewBinding
    private val TAG = "AAA"
    private var id: String = "7462562"
    private var type: String = "all"
    private lateinit var mMap: GoogleMap
    private var latitude = 41.326298
    private var longitude = 69.228560
    lateinit var client: FusedLocationProviderClient
    lateinit var productAdapter: ProductAdapter
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("key") as String
            type = it.getString("type") as String
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            FragmentProductViewBinding.inflate(layoutInflater)
        appDatabase = AppDatabase.getDatabase(requireContext())
        binding.nestedScroll.fullScroll(View.FOCUS_UP)
        binding.nestedScroll.scrollTo(0, 0)
        binding.appBar.setExpanded(true)
        Dexter.withContext(binding.root.context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
        val mapFragment =
            this.childFragmentManager.findFragmentById(R.id.fallasMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        loadProduct(type, id).observe(viewLifecycleOwner, Observer
        {
            when (it.status) {
                Status.LOADING -> {
                    binding.nestedScroll.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    type = it.data?.type.toString()
                    product = it.data
                    binding.nestedScroll.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    Picasso.get().load(it.data?.image).placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(binding.image)
                    binding.date.text = it.data?.date
                    binding.productName.text = it.data?.productName
                    binding.productSalary.text = it.data?.salary
                    binding.productAbout.text = it.data?.description
                    latitude = it.data?.latitude?.toDouble() ?: 41.326298
                    longitude = it.data?.longitude?.toDouble() ?: 69.228560
                    binding.userName.text = it.data?.userName
                    binding.locateName.text = it.data?.sity
                    getLike()
                }
                Status.ERROR -> {
                    binding.nestedScroll.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.errorImage.visibility = View.VISIBLE
                }
            }
        })
        binding.backHome.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "OLX")
            intent.putExtra(Intent.EXTRA_TEXT,
                "https://t.me/ismoilov9922")
            startActivity(Intent.createChooser(intent, "choose one"))
        }
        binding.call.setOnClickListener {
            LoadProduct(requireContext()).permissionCall(product?.number.toString())
        }
        binding.smsSend.setOnClickListener {
            try {
                val uri = Uri.parse("smsto:${product?.number}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "")
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, "Do not SMS Error!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.like.setOnClickListener {
            if (appDatabase.productDao().getProductForLike().contains(product)) {
                binding.like.setImageResource(R.drawable.ic_like)
                appDatabase.productDao().deleteProduct(product!!)

            } else {
                appDatabase.productDao().insertProduct(product!!)
                binding.like.setImageResource(R.drawable.ic_liked)
            }
        }

        LoadProduct(requireContext()).loadProduct(type).observe(viewLifecycleOwner, Observer
        {
            when (it.status) {
                Status.LOADING -> {
                    binding.rvProduct.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.nestedScroll.visibility = View.INVISIBLE
                }
                Status.ERROR -> {
                    binding.rvProduct.visibility = View.GONE
                    binding.errorImage.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.rvProduct.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.errorImage.visibility = View.GONE
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
                                            binding.like.setImageResource(R.drawable.ic_like)
                                            appDatabase.productDao().deleteProduct(product)

                                        } else {
                                            appDatabase.productDao().insertProduct(product)
                                            binding.like.setImageResource(R.drawable.ic_liked)
                                        }
                                    }
                                })
                        binding.rvProduct.adapter = productAdapter
                    } else {
                        binding.rvProduct.visibility = View.GONE
                        binding.errorImage.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
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

    fun getLike() {
        appDatabase.productDao().getProduct().observe(viewLifecycleOwner, Observer {
            if (it.contains(product)) {
                binding.like.setImageResource(R.drawable.ic_liked)
            } else {
                binding.like.setImageResource(R.drawable.ic_like)
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val myLocation = LatLng(20.5937, 78.9629)
        mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in India"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun loadProduct(type: String, id: String): LiveData<Resource<Product>> {
        val productData = MutableLiveData<Resource<Product>>()
        if (NetworkHelper(binding.root.context).isConnected()) {
            productData.postValue(Resource.loading(null))
            try {
                FirebaseFirestore.getInstance().collection(type)
                    .whereEqualTo("id", id).get()
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            val product = result.toObjects(Product::class.java)
                            productData.postValue(Resource.success(product[0]))
                        }
                    }.addOnFailureListener {

                    }
            } catch (e: Exception) {
                productData.postValue(Resource.error("Error!!!", null))
            }
        }
        return productData
    }
}