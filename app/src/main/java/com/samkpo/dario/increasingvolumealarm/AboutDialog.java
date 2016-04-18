/*
 * Copyright (C) 2016 Aguilera Dario
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.samkpo.dario.increasingvolumealarm;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.DialogPreference;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.Type;

public class AboutDialog extends DialogPreference {
    private static final String TAG = "AboutDialog";

    public AboutDialog(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Set the layout
        setDialogLayoutResource(R.layout.about_dialog);

        //A simple ok button, no need for a cancel button, it's an informative dialog
        setPositiveButtonText(context.getString(R.string.action_ok));
        setNegativeButtonText(null);

        //Set dialog title
        setDialogTitle(getContext().getString(R.string.app_name));

        //Lets set the summary
        setSummary(String.format(
                getContext().getString(R.string.about_app_summary),
                (BuildConfig.DEBUG ? BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE
                                   : BuildConfig.VERSION_NAME),
                getContext().getString(R.string.currentBuildYear)
        ));
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        //About the app and me, the author
        TextView content = (TextView)view.findViewById(R.id.about_text_view);
        content.setText(Html.fromHtml(String.format(
                        view.getResources().getString(R.string.about_body),
                        "Aguilera Darío",
                        "dario_21_06@hotmail.com"))
        );
        content.setMovementMethod(LinkMovementMethod.getInstance());

        TextView _changelog = (TextView)view.findViewById(R.id.about_context_tv);
        _changelog.setText(view.getResources().getString(R.string.about_app_changelog));

        //Show the app changelog
        TextView content2 = (TextView)view.findViewById(R.id.about_text_view2);
        content2.setTypeface(Typeface.MONOSPACE);
        content2.setMovementMethod(LinkMovementMethod.getInstance());
        content2.setText(getDialogText());
    }

    //Get the changelog from a raw file (Code stolen from cyanogenmod filemanager
    private String getDialogText(){
        String _cadena = null;
        InputStream is = getContext().getResources().openRawResource(R.raw.changelog);

        if (is == null) {
            Log.e(TAG, "Changelog file not exists"); //$NON-NLS-1$
            return null;
        }

        try {
            // Read the change log
            StringBuilder sb = new StringBuilder();
            int read = 0;
            byte[] data = new byte[512];
            while ((read = is.read(data, 0, 512)) != -1) {
                sb.append(new String(data, 0, read));
            }

            //Lets return the string
            _cadena = sb.toString();

        } catch (Exception e) {
            Log.e(TAG, "Failed to read changelog file", e);

        } finally {
            try {
                is.close();
                return _cadena;
            } catch (Exception e) {
                Log.e(TAG, "Error while closing string builder", e);
                return null;
            }
        }
    }
}
