package uz.pdp.olxuz.register.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import uz.pdp.olxuz.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        var yourPreference: YourPreference = YourPreference.getInstance(binding.root.context)
//        var auth: FirebaseAuth = FirebaseAuth.getInstance()
//        binding.numberBtn.setOnClickListener {
//            binding.progressbar.visibility = View.VISIBLE
//            binding.numberBtn.visibility = View.INVISIBLE
//            val number = binding.telNumber.text.toString()
//            val intent = Intent(this, RegisterActivity::class.java)
//            intent.putExtra("telnumber", number)
//            intent.putExtra("username", number)
//            startActivity(intent)
//            finish()
//        }
    }

}