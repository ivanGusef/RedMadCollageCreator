package com.ivangusev.rmcc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ivangusev.rmcc.R;
import com.ivangusev.rmcc.client.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by ivan on 22.02.14.
 */
public class UserDetailsFragment extends Fragment {

    public static final String ARG_USER = "arg_user";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_details, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View contentView = getView();
        final Bundle args = getArguments();
        final User user = args.getParcelable(ARG_USER);
        ((TextView) contentView.findViewById(R.id.user_name)).setText(user.getUserName());
        ((TextView) contentView.findViewById(R.id.full_name)).setText(user.getFullName());

        final ImageView profilePictureView = ((ImageView) contentView.findViewById(R.id.profile_picture));
        ImageLoader.getInstance().displayImage(user.getProfilePicture(), profilePictureView);
    }
}
