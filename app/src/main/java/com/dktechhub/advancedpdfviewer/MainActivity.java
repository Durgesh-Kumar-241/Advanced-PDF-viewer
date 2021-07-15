package com.dktechhub.advancedpdfviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;

public class MainActivity extends AppCompatActivity {
    boolean nightMode=true;
    int defaultPage=0;
    PDFView pdfView;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfView = findViewById(R.id.pdfView);
        try{
            Intent intent= getIntent();
            uri=intent.getData();
            Toast.makeText(this, intent.getData().toString(), Toast.LENGTH_SHORT).show();
            load();
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
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        defaultPage=page;
                    }
                })
                .linkHandler(new LinkHandler() {
                    @Override
                    public void handleLinkEvent(LinkTapEvent event) {
                        Toast.makeText(MainActivity.this, event.getLink().toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options_menu, menu);
        menu.findItem(R.id.night_mode)
                .setIcon(R.drawable.ic_baseline_nights_stay_24);
        menu.findItem(R.id.listen).setIcon(R.drawable.ic_baseline_audiotrack_24);
        menu.findItem(R.id.search).setIcon(R.drawable.ic_search_black_24dp);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.night_mode:
                nightMode=!nightMode;
                load();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}