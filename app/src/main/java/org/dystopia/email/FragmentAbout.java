package org.dystopia.email;

/*
    This file is part of FairEmail.

    FairEmail is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FairEmail is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FairEmail.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2018, Marcel Bokhorst (M66B)
    Copyright 2018-2020, Distopico (dystopia project) <distopico@riseup.net> and contributors
*/

import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import org.dystopia.email.util.CompatibilityUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.mail.Address;

public class FragmentAbout extends FragmentEx {
  private TextView tvVersion;
  private Button btnLog;
  private Button btnDebugInfo;

  @Override
  @Nullable
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    setSubtitle(R.string.menu_about);

    View view = inflater.inflate(R.layout.fragment_about, container, false);

    tvVersion = view.findViewById(R.id.tvVersion);
    btnLog = view.findViewById(R.id.btnLog);
    btnDebugInfo = view.findViewById(R.id.btnDebugInfo);

    int version = R.string.title_version;
    String versionName = getString(version, BuildConfig.VERSION_NAME);
    tvVersion.setText(versionName);

    btnLog.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                .replace(R.id.content_frame, new FragmentLogs())
                .addToBackStack("logs");
            fragmentTransaction.commit();
          }
        });

    btnDebugInfo.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            btnDebugInfo.setEnabled(false);
            new SimpleTask<Long>() {
              @Override
              protected Long onLoad(Context context, Bundle args)
                  throws UnsupportedEncodingException {
                  return null;
              }

              @Override
              protected void onLoaded(Bundle args, Long id) {
                btnDebugInfo.setEnabled(true);
                if (id != null) {
                  startActivity(
                      new Intent(getContext(), ActivityCompose.class)
                          .putExtra("action", "edit")
                          .putExtra("id", id));
                }
              }

              @Override
              protected void onException(Bundle args, Throwable ex) {
                btnDebugInfo.setEnabled(true);
                Helper.unexpectedError(getContext(), ex);
              }
            }.load(FragmentAbout.this, new Bundle());
          }
        });

    return view;
  }
}
