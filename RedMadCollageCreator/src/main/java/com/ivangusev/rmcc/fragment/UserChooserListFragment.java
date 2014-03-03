package com.ivangusev.rmcc.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.ivangusev.rmcc.R;
import com.ivangusev.rmcc.client.HttpManager;
import com.ivangusev.rmcc.client.exception.HttpException;
import com.ivangusev.rmcc.client.model.User;
import com.ivangusev.rmcc.client.task.AsyncResult;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 21.02.14.
 */
public class UserChooserListFragment extends ListFragment implements SearchView.OnQueryTextListener {

    public static interface OnUserChooseListener {
        void onUserChosen(User user);
    }

    private OnUserChooseListener mListener;
    private SearchAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener = (OnUserChooseListener) getActivity();

        setHasOptionsMenu(true);
        mAdapter = new SearchAdapter(getActivity());
        setListAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.m_user_chooser, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.action_search));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mAdapter.getFilter().filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mAdapter.getFilter().filter(s);
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onUserChosen(mAdapter.getItem(position));
    }

    private static class SearchAdapter extends BaseAdapter implements Filterable {

        private static final String SEARCH_URL = "https://api.instagram.com/v1/users/search";

        private final Context mContext;
        private List<User> mUsers;

        private SearchAdapter(Context context) {
            this.mContext = context;
            this.mUsers = new ArrayList<User>();
        }

        public void reset(List<User> users) {
            if (users == null) users = new ArrayList<User>();

            if (users.size() == mUsers.size()) {
                mUsers = users;
                notifyDataSetInvalidated();
            } else {
                mUsers = users;
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mUsers.size();
        }

        @Override
        public User getItem(int position) {
            return mUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(mUsers.get(position).getId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) convertView;
            if(view == null) {
                view = (TextView) LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null);
            }
            view.setText(getItem(position).getUserName());

            return view;
        }

        @Override
        public Filter getFilter() {
            return new UserFilter();
        }

        class UserFilter extends Filter {

            private final String TAG = UserFilter.class.getName();

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint == null || constraint.length() == 0) return null;

                final FilterResults results = new FilterResults();
                final HttpManager httpManager = HttpManager.getInstance(mContext);
                final List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
                httpParams.add(new BasicNameValuePair("q", String.valueOf(constraint)));
                try {
                    final JSONArray jUsers = httpManager.doGet(SEARCH_URL, httpParams).optJSONArray("data");

                    final List<User> users = new ArrayList<User>();
                    final int usersCount = jUsers.length();
                    results.count = usersCount;
                    for (int i = 0; i < usersCount; i++) {
                        users.add(new User(jUsers.optJSONObject(i)));
                    }
                    results.values = users;
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint == null || constraint.length() == 0) return;
                if (results == null) {
                    reset(null);
                    return;
                }

                reset((List<User>) results.values);
            }
        }
    }
}
