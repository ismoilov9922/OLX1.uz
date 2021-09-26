package uz.pdp.olxuz.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import uz.pdp.olxuz.R
import uz.pdp.olxuz.databinding.FragmentProfileBinding
import uz.pdp.olxuz.utils.LoadProduct
import uz.pdp.olxuz.utils.Status

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentProfileBinding

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        LoadProduct(requireContext()).loadProduct("myProduct")
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data != null) {
                            binding.myProductCount.text = it.data.size.toString()
                        }
                    }
                }
            })
        binding.addProduct.setOnClickListener {
            findNavController().navigate(R.id.addProductFragment)
        }
        binding.activeComment.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.smsFragment)
        }
        binding.myProduct.setOnClickListener {
            findNavController().navigate(R.id.myProductFragment)
        }
        binding.rating.setOnClickListener {
            Toast.makeText(requireContext(), "Sizda xali baho olinmagan!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.balance.setOnClickListener {
            Toast.makeText(requireContext(), "Xamyoningizda 0.0 so'm pul bor!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }
        binding.help.setOnClickListener {
            val url = Uri.parse("https://help.olx.uz/hc/uz")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        binding.termsConditions.setOnClickListener {
            val url = Uri.parse("https://help.olx.uz/hc/ru/articles/360009197178")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        binding.userCovenant.setOnClickListener {
            val url =
                Uri.parse("https://help.olx.uz/hc/uz/sections/360002533258-Maxfiylik-siyosati")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        binding.about.setOnClickListener {
            val url =
                Uri.parse("https://help.olx.uz/hc/ru/sections/360002647937-%D0%9E-%D0%BA%D0%BE%D0%BC%D0%BF%D0%B0%D0%BD%D0%B8%D0%B8-Kompaniya-haqida")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}