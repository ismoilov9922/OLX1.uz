package uz.pdp.olxuz.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import uz.pdp.olxuz.MainActivity
import uz.pdp.olxuz.R
import uz.pdp.olxuz.databinding.FragmentSettingBinding
import uz.pdp.olxuz.databinding.ModuleRemoveDialogBinding
import uz.pdp.olxuz.sharedPreference.YourPreference

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentSettingBinding
    lateinit var yourPreference: YourPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        yourPreference = YourPreference.getInstance(binding.root.context)
        if (!yourPreference.getTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.themeSwitch.isChecked = false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeSwitch.isChecked = true
        }
        binding.themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.themeSwitch.isChecked = true
                yourPreference.setTheme(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.themeSwitch.isChecked = false
                yourPreference.setTheme(false)
            }
        }
        binding.share.setOnClickListener {
            intentMessageTelegram("https://t.me/ismoilov9922")
        }
        binding.info.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val binding1 = ModuleRemoveDialogBinding.inflate(inflater, null, false)
            builder.setView(binding1.root)
            val alertDialog = builder.create()
            alertDialog.show()
        }
        return binding.root
    }

    fun intentMessageTelegram(msg: String?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "OLX faol e'lonlar")
        intent.putExtra(Intent.EXTRA_TEXT, msg)
        context?.startActivity(Intent.createChooser(intent, "choose one"))
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