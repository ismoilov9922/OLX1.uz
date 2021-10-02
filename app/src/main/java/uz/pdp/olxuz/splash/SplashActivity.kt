package uz.pdp.olxuz.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.pdp.olxuz.MainActivity
import uz.pdp.olxuz.databinding.ActivitySpalshBinding
import uz.pdp.olxuz.register.login.LoginActivity
import uz.pdp.olxuz.sharedPreference.YourPreference
import java.util.concurrent.Executors

class SplashActivity : AppCompatActivity() {
    private lateinit var yourPreference: YourPreference
    private lateinit var binding: ActivitySpalshBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpalshBinding.inflate(layoutInflater)
        setContentView(binding.root)
        yourPreference = YourPreference.getInstance(binding.root.context)
        Executors.newSingleThreadExecutor().execute {
            Thread.sleep(2000)
            if (yourPreference.getData("phone")!!.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}