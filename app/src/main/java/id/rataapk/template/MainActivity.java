package id.rataapk.template;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "extra_mode";
    public static final String MODE_ADMIN = "admin";
    public static final String MODE_USER = "user";

    private String mode = MODE_USER;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode = getIntent().getStringExtra(EXTRA_MODE);
        if (mode == null) mode = MODE_USER;

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            String judul = getString(R.string.app_name)
                    + (MODE_ADMIN.equals(mode) ? " \u2022 " + getString(R.string.admin_mode_badge) : "");
            getSupportActionBar().setTitle(judul);
        }

        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);

        // Tetap di dalam WebView untuk semua navigasi, tidak lempar ke browser luar
        webView.setWebViewClient(new WebViewClient());

        // Konten website hasil upload (workflow menaruhnya di assets/www/)
        webView.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Menu "Kelola PIN Pengguna" hanya tampak untuk admin
        MenuItem kelolaPin = menu.findItem(R.id.menu_kelola_pin);
        kelolaPin.setVisible(MODE_ADMIN.equals(mode));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_kelola_pin) {
            startActivity(new Intent(this, AdminActivity.class));
            return true;
        } else if (id == R.id.menu_keluar) {
            keluarKePinScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void keluarKePinScreen() {
        Intent intent = new Intent(this, PinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
