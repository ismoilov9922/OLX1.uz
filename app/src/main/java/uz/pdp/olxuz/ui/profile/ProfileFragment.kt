package uz.pdp.olxuz.ui.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import uz.pdp.olxuz.R
import uz.pdp.olxuz.databinding.FragmentProfileBinding

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
        activity?.window?.statusBarColor = TRANSPARENT
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