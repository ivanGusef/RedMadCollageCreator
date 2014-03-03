package com.ivangusev.rmcc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import com.ivangusev.rmcc.R;
import com.ivangusev.rmcc.client.model.User;
import com.ivangusev.rmcc.fragment.UserChooserListFragment;
import com.ivangusev.rmcc.fragment.UserDetailsFragment;

/**
 * Created by ivan on 21.02.14.
 */
public class UserChooserActivity extends ActionBarActivity implements UserChooserListFragment.OnUserChooseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chooser);

        if (savedInstanceState == null) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, new UserChooserListFragment());
            transaction.commit();
        }
    }

    @Override
    public void onUserChosen(User user) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        final Bundle args = new Bundle();
        args.putParcelable(UserDetailsFragment.ARG_USER, user);
        transaction.replace(R.id.content_frame, Fragment.instantiate(this, UserDetailsFragment.class.getName(), args));
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
}
