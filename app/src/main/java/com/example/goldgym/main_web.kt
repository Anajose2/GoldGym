package com.example.goldgym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main_web.*

class main_web : AppCompatActivity() {
    private var BASE_URL = "https://google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_web)

        val extras = intent.extras
        BASE_URL =  extras?.getString("DIRECCION").toString().trim()?:"sin conexion web"

        //WebView
        //definimos el cliente chrome
        webView.webChromeClient = object : WebChromeClient(){

        }

        //ViewClient
        webView.webViewClient = object : WebViewClient(){

        }
        val settings = webView.settings
        settings.javaScriptEnabled = true

        webView.loadUrl(BASE_URL)

    }
}