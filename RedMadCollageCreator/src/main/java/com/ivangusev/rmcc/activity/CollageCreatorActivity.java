package com.ivangusev.rmcc.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ivangusev.rmcc.R;
import com.ivangusev.rmcc.view.GestureImageView;

/**
 * Created by ivan on 23.02.14.
 */
public class CollageCreatorActivity extends ActionBarActivity {

    private FrameLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_creator);

        mContent = (FrameLayout) findViewById(R.id.content_frame);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_collage_creator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mi_add_item) {
            final GestureImageView view = new GestureImageView(this);
            final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view.setScaleType(ImageView.ScaleType.MATRIX);
            view.setImageResource(R.drawable.icon_sample);
            view.setLayoutParams(params);
            mContent.addView(view);
            return true;
        }
        return false;
    }
}
