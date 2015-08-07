package com.lovejoy777.rommate.fonts;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lovejoy777.rommate.R;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.io.IOException;

/**
 * Created by lovejoy777 on 29/06/15.
 */
public class Screen1Fonts extends AppCompatActivity {

    CardView card1, card2, card3, card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1fonts);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);


        card1 = (CardView) findViewById(R.id.CardView_fontscard1);
        card2 = (CardView) findViewById(R.id.CardView_fontscard2);
        card3 = (CardView) findViewById(R.id.CardView_fontscard3);
        card4 = (CardView) findViewById(R.id.CardView_fontscard4);

        // CARD 1
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent freeactivity = new Intent(Screen1Fonts.this, FreeFonts.class);

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(freeactivity, bndlanimation);

            }
        }); // end card1

        // CARD 2
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent donateactivity = new Intent(Screen1Fonts.this, DonateFonts.class);

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(donateactivity, bndlanimation);


            }
        }); // end card3

        // CARD 3
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent installactivity = new Intent(Screen1Fonts.this, InstallFonts.class);

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(installactivity, bndlanimation);

            }
        }); // end card3

        // CARD 4
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Restore().execute();

            }
        }); // end card4

    } // ends onCreate




    private class Restore extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressRestore;

        protected void onPreExecute() {

            progressRestore = ProgressDialog.show(Screen1Fonts.this, "Restoring Originals",
                    "restoring...", true);
        }

        @Override
        protected Void doInBackground(Void... params) {

            //ProgressDialog progressRestore;
            String checkpath = getApplicationInfo().dataDir + "/backups/fonts";
            String startpathfonts = getApplicationInfo().dataDir + "/backup/fonts/fonts";
            String fonts = "/system/fonts";



            // CREATES /SDCARD/OVERLAYS
            File dir1 = new File(checkpath);
            if (!dir1.exists()) {

                Toast.makeText(Screen1Fonts.this, "no original fonts found", Toast.LENGTH_LONG).show();

            } else {

                try {
                    RootTools.remount("/system", "RW");

                      RootTools.copyFile(startpathfonts, fonts + "/", true, true);
                    //  RootCommands.moveCopyRoot(startpath, media + "/");

                    RootTools.remount("/system", "RO");
                    // CLOSE ALL SHELLS
                    RootTools.closeAllShells();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            //  finish();
            return null;
        }


        protected void onPostExecute(Void result) {

            progressRestore.dismiss();

            //finish();

            // LAUNCH LAYERS.CLASS
            overridePendingTransition(R.anim.back2, R.anim.back1);
            Intent iIntent = new Intent(Screen1Fonts.this, Screen1Fonts.class);
            iIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            iIntent.putExtra("ShowSnackbar", true);
            iIntent.putExtra("SnackbarText", "Installed selected Fonts");
            startActivity(iIntent);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.back2, R.anim.back1);
            return true;
        }
        return false;
    }
}