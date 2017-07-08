package shafir.irena.xmlandfirbase;


import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class YnetDetailFragment extends Fragment {
    //properties:
    private WebView webView;
    private ProgressBar progressBar;

    // constants
    private static final String ARG_URL = "url";

    public YnetDetailFragment() {
        // Required empty public constructor
    }

    public static YnetDetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        YnetDetailFragment fragment = new YnetDetailFragment();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ynet_detail, container, false);
        webView = (WebView) v.findViewById(R.id.webView);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        final String url = getArguments().getString(ARG_URL);

        // java script
        webView.getSettings().setJavaScriptEnabled(true);

        // redirect...
//        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webView.loadUrl(request.getUrl().toString());
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                Log.d("NessUrl", url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setProgress(View.VISIBLE);
            }
        });

        webView.loadUrl(url);

        return v;
    }

}
