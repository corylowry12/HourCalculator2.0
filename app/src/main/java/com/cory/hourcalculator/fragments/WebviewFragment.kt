package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.LinkClass
import com.google.android.material.appbar.MaterialToolbar
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.webkit.WebSettings
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import com.cory.hourcalculator.classes.DarkThemeData
import com.cory.hourcalculator.classes.Vibrate
import kotlinx.coroutines.delay
import java.util.*
import kotlin.concurrent.schedule


class WebviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = requireActivity().findViewById<WebView>(R.id.webView)
        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
        val swipeRefreshLayout =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        val appBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarWebView)

        Handler(Looper.getMainLooper()).postDelayed({

            appBar?.setNavigationOnClickListener {
                Vibrate().vibration(requireContext())
                webView?.stopLoading()
                progressBar?.visibility = View.GONE
                activity?.supportFragmentManager?.popBackStack()
            }

            val url = LinkClass(requireContext()).loadLink().toString()

            appBar?.setOnMenuItemClickListener {
                Vibrate().vibration(requireContext())
                when (it.itemId) {
                    R.id.refresh -> {
                        Vibrate().vibration(requireContext())
                        webView?.reload()
                        true
                    }
                    R.id.copyMenu -> {
                        Vibrate().vibration(requireContext())
                        val clipBoard =
                            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("URL", url) //intent.getStringExtra("url")
                        clipBoard.setPrimaryClip(clip)
                        Toast.makeText(
                            requireContext(),
                            "Text copied to clipboard",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                    R.id.shareMenu -> {
                        Vibrate().vibration(requireContext())
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
                        startActivity(Intent.createChooser(shareIntent, "Share Via..."))
                        true
                    }
                    else -> false
                }
            }

            webView.onResume()

            webView.loadUrl(url)

            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val newUrl = request?.url.toString()
                    webView.loadUrl(newUrl)
                    return true
                }
            }
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                webView.settings.forceDark = WebSettings.FORCE_DARK_ON
            }

            swipeRefreshLayout.setOnRefreshListener {
                webView.reload()
            }
        }, 800)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView!!.canGoBack()) {
                    webView.goBack()
                }
                else {
                    webView.stopLoading()
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        })
    }
}