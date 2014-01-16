package com.decad3nce.aegis.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;
import com.decad3nce.aegis.R;
import com.decad3nce.aegis.Utils;

public class AboutFragment extends Fragment {
    private Button onClickEmail, onClickGithub, onClickXDA, onClickQuickly;
    private ShareActionProvider mShareActionProvider;
    private ListView mButtonList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.about_layout, container, false);
        setHasOptionsMenu(true);
        String version = "UKNOWN";
        String[] values = new String[] { getResourceString(R.string.about_email_link), getResourceString(R.string.about_github_link), getResourceString(R.string.about_xda_link), getResourceString(R.string.about_changelog_link), getResourceString(R.string.overflow_licenses)};
        mButtonList = (ListView) mainView.findViewById(R.id.about_list);
        mButtonList.setAdapter(new aboutImageAdapter(getActivity(), values));
        mButtonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent i;
            String url;

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        final Intent emailIntent = new Intent(
                            android.content.Intent.ACTION_SEND);

                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                new String[] { "the1andonlydave@gmail.com" });
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                "aeGis SmartWatch Questions");
                        startActivity(Intent.createChooser(emailIntent, "Email"));
                        break;
                    case 1:
                        url = "http://www.github.com/The1andONLYdave/Aegis";
                        i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case 2:
                        url = "http://forum.xda-developers.com/showthread.php?t=2038762";
                        i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case 3:
                        Utils.createWebViewDialog("file:///android_asset/changelog.html", getActivity());
                        break;
                    case 4:
                        Utils.createWebViewDialog("file:///android_asset/licenses.html", getActivity());
                        break;
                }
            }
        });
        try {
            version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            //Shouldn't happen
        }

        TextView tV, textView;
        tV = (TextView) mainView.findViewById(R.id.currentversion);
        textView = (TextView) mainView.findViewById(R.id.developername);
        tV.setText(version);
        tV.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf"));
        textView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf"));
        return mainView;
    }

    public void onPrepareOptionsMenu(Menu menu) {
        //gdamnit android
        menu.clear();
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);

        MenuItem item = menu.findItem(R.id.about_menu_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent i;
        i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "aeGis");
        String mInfo = "\nCheck out original aeGis!\n\n";
        mInfo = mInfo + "https://play.google.com/store/apps/details?id=com.decad3nce.aegis \n\n";
        i.putExtra(Intent.EXTRA_TEXT, mInfo);

        setShareIntent(i);
        super.onPrepareOptionsMenu(menu);
    }

    private String getResourceString(int id) {
        String resourceString = getActivity().getResources().getString(id);
        return resourceString;
    }

    private void setShareIntent(Intent shareIntent) {
        if(mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about_menu_quickly:
                String url = "market://search?q=pub:Decad3nce";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
        }
        return false;
    }

    private class aboutImageAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;
        Typeface tf;
        private final Integer[] images = {R.drawable.ic_content_email, R.drawable.ic_github, R.drawable.ic_xda, R.drawable.ic_action_about, R.drawable.ic_licenses};


        public aboutImageAdapter(Context context, String[] values) {
            super(context, R.layout.about_list_item_layout, values);
            this.context = context;
            this.values = values;
            this.tf = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.about_list_item_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.about_text_view);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.about_image_view);

            textView.setText(values[position]);
            textView.setTypeface(tf);
            imageView.setImageResource(images[position]);

            return rowView;
        }
    }

}
