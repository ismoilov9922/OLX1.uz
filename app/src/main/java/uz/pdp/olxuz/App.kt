package uz.pdp.olxuz

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import uz.pdp.olxuz.sharedPreference.YourPreference

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val yourPreference = YourPreference.getInstance(this)
        if (!yourPreference.getTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}