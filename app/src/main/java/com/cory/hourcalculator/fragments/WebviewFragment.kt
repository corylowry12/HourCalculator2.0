package com.cory.hourcalculator.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cory.hourcalculator.R
import com.cory.hourcalculator.classes.*
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class WebviewFragment : Fragment() {

    var themeSelection = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val darkThemeData = DarkThemeData(requireContext())
        when {
            darkThemeData.loadDarkModeState() == 1 -> {
                activity?.setTheme(R.style.Theme_DarkTheme)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 0 -> {
                activity?.setTheme(R.style.Theme_MyApplication)
                themeSelection = false
            }
            darkThemeData.loadDarkModeState() == 2 -> {
                activity?.setTheme(R.style.Theme_AMOLED)
                themeSelection = true
            }
            darkThemeData.loadDarkModeState() == 3 -> {
                when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        activity?.setTheme(R.style.Theme_MyApplication)
                        themeSelection = false
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        activity?.setTheme(AccentColor(requireContext()).followSystemTheme(requireContext()))
                        themeSelection = true
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        activity?.setTheme(R.style.Theme_AMOLED)
                        themeSelection = true
                    }
                }
            }
        }

        val accentColor = AccentColor(requireContext())
        val followSystemVersion = FollowSystemVersion(requireContext())

        when {
            accentColor.loadAccent() == 0 -> {
                activity?.theme?.applyStyle(R.style.teal_accent, true)
            }
            accentColor.loadAccent() == 1 -> {
                activity?.theme?.applyStyle(R.style.pink_accent, true)
            }
            accentColor.loadAccent() == 2 -> {
                activity?.theme?.applyStyle(R.style.orange_accent, true)
            }
            accentColor.loadAccent() == 3 -> {
                activity?.theme?.applyStyle(R.style.red_accent, true)
            }
            accentColor.loadAccent() == 4 -> {
                if (!followSystemVersion.loadSystemColor()) {
                    activity?.theme?.applyStyle(R.style.system_accent, true)
                }
                else {
                    if (themeSelection) {
                        activity?.theme?.applyStyle(R.style.system_accent_google, true)
                    }
                    else {
                        activity?.theme?.applyStyle(R.style.system_accent_google_light, true)
                    }
                }
            }
        }
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

        val connectivityManager = (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }

        if (connectivityManager) {
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
                        R.id.openBrowser -> {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(browserIntent)
                            true
                        }
                        R.id.refresh -> {
                            webView?.reload()
                            true
                        }
                        R.id.copyMenu -> {
                            val clipBoard =
                                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("URL", url)
                            clipBoard.setPrimaryClip(clip)
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_copied_to_clipboard),
                                Toast.LENGTH_SHORT
                            ).show()
                            true
                        }
                        R.id.shareMenu -> {
                            val shareIntent = Intent()
                            shareIntent.action = Intent.ACTION_SEND
                            shareIntent.type = "text/plain"
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
                            startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    getString(R.string.share_via)
                                )
                            )
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

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
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
        }
        else {
            webView.stopLoading()
            activity?.supportFragmentManager?.popBackStack()
            Toast.makeText(requireContext(), getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show()
        }
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView!!.canGoBack()) {
                        webView.goBack()
                    } else {
                        webView.stopLoading()
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
            })
    }
}