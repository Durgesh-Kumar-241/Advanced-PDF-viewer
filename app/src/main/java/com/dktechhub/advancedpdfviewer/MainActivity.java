package com.dktechhub.advancedpdfviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;

public class MainActivity extends AppCompatActivity  {
    boolean nightMode=true;
    int defaultPage=0;
    SharedPreferences sharedPreferences;
    PDFView pdfView;
    ActionBar actionBar;
    Uri uri;
    SeekBar seekBar;
    //FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();
        setContentView(R.layout.activity_main);
        pdfView = findViewById(R.id.pdfView);
        actionBar = getSupportActionBar();
        seekBar=findViewById(R.id.seekBar);

        if(actionBar!=null)//fab=findViewById(R.id.fab);
            actionBar.hide();
        try{
            Intent intent= getIntent();
            uri=intent.getData();
            //Toast.makeText(this, intent.getData().toString(), Toast.LENGTH_SHORT).show();
            load();

            //fab.setOnClickListener(this::onClick);
        }catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void load()
    {
        pdfView.fromUri(uri)
                .nightMode(nightMode)
                .defaultPage(defaultPage)
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {
                        if(actionBar!=null)
                        {
                            if(actionBar.isShowing())
                            {  actionBar.hide();
                            seekBar.setVisibility(View.GONE);}
                            else {
                                actionBar.show();
                                seekBar.setVisibility(View.VISIBLE);
                            }
                        }
                        return false;
                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        seekBar.setMax(nbPages);
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                defaultPage=progress;

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                load();
                            }
                        });
                    }
                })
                .onPageScroll(new OnPageScrollListener() {
                    @Override
                    public void onPageScrolled(int page, float positionOffset) {

                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        defaultPage=page;
                    }
                })
                .linkHandler(event -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getLink().getUri()));
                        startActivity(Intent.createChooser(intent, "Open browser with"));
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(MainActivity.this, event.getLink().toString(), Toast.LENGTH_SHORT).show();
                })
                .load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options_menu, menu);
        menu.findItem(R.id.night_mode).setChecked(nightMode)
                .setIcon(R.drawable.ic_baseline_nights_stay_24);

        menu.findItem(R.id.search).setIcon(R.drawable.ic_search_black_24dp);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.night_mode) {
            sharedPreferences.edit().putBoolean("dark_theme",!nightMode).apply();
            recreate();

            load();
        }
        return super.onOptionsItemSelected(item);
    }







    public void applyTheme() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nightMode = sharedPreferences.getBoolean("dark_theme", false);

        if (!nightMode) setTheme(R.style.Theme_AdvancedPDFViewer);
        else setTheme(R.style.Theme_AdvancedPDFViewer_night);
    }
}