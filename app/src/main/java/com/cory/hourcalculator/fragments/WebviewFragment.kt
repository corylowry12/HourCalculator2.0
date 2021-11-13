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
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
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

        Handler().postDelayed({
            val webview = activity?.findViewById<WebView>(R.id.webView)
            val progressBar = activity?.findViewById<ProgressBar>(R.id.progressBar)
            val swipeRefreshLayout =
                activity?.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

            val appBar = activity?.findViewById<MaterialToolbar>(R.id.materialToolBarWebView)

            appBar?.setNavigationOnClickListener {
                webview?.stopLoading()
                progressBar?.visibility = View.GONE
                activity?.supportFragmentManager?.popBackStack()
            }

            val url = LinkClass(requireContext()).loadLink().toString()

            appBar?.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.refresh -> {
                        webview?.reload()
                        true
                    }
                    R.id.copyMenu -> {
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
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "URL")
                        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
                        startActivity(Intent.createChooser(shareIntent, "Share Via..."))
                        true
                    }
                    else -> false
                }
            }

            webview?.onResume()

            webview?.loadUrl(url)

            webview?.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar?.visibility = View.VISIBLE
                    swipeRefreshLayout?.isRefreshing = false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar?.visibility = View.GONE
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    webview?.loadUrl(url)
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            webview?.settings?.javaScriptEnabled = true
            webview?.settings?.domStorageEnabled = true
            webview?.settings?.javaScriptCanOpenWindowsAutomatically = true

            swipeRefreshLayout?.setOnRefreshListener {
                webview?.reload()
            }
        }, 800)
    }
}