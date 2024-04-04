package com.claudio.androidiii

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import androidx.appcompat.app.AppCompatActivity
import com.claudio.androidiii.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMemoryInfo()

        binding.btSum.setOnClickListener {
//            binding.tvResult.text = sum(binding.edtValue1.text.toString().toInt(), binding.edtValue2.text.toString().toInt()).toString()
            openWhatsApp(this)
        }

        println(BuildConfig.ENDPOINT_URL)
    }

    fun sum(a: Int, b: Int): Int {
        return a + b
    }

    private fun getMemoryInfo(): String {
        val path = Environment.getDataDirectory() // Obtém o diretório de armazenamento interno do dispositivo
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong

        val totalBytes = totalBlocks * blockSize // Espaço total em bytes
        val availableBytes = availableBlocks * blockSize // Espaço disponível em bytes
        val usedBytes = totalBytes - availableBytes // Espaço ocupado em bytes

        val totalGB = totalBytes / (1024 * 1024 * 1024) // Espaço total em gigabytes
        val availableGB = availableBytes / (1024 * 1024 * 1024) // Espaço disponível em gigabytes
        val usedGB = usedBytes / (1024 * 1024 * 1024) // Espaço ocupado em gigabytes

        println("Espaço total: $totalGB GB\nEspaço disponível: $availableGB GB\nEspaço ocupado: $usedGB GB")

        return "Espaço total: $totalGB GB\nEspaço disponível: $availableGB GB\nEspaço ocupado: $usedGB GB"
    }

    fun openWhatsApp(context: Context) {
        val packageName = "com.instagram.android"
        val url = "https://play.google.com/store/apps/details?id=$packageName"

        val packageManager = context.packageManager
        val isWhatsAppInstalled: Boolean = try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            println("Errorrr ${e.message}")
            false
        }

        if (isWhatsAppInstalled) {
            // Abrir o WhatsApp
            val intent = Intent(Intent.ACTION_MAIN)
            intent.setPackage(packageName)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            context.startActivity(intent)
        } else {
            // Redirecionar para a página de download na Play Store
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
    }

}