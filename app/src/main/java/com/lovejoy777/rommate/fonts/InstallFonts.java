package com.lovejoy777.rommate.fonts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lovejoy777.rommate.MainActivity;
import com.lovejoy777.rommate.R;
import com.lovejoy777.rommate.commands.RootCommands;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by lovejoy777 on 29/06/15.
 */
public class InstallFonts extends AppCompatActivity {

    static final String TAG = "InstallFontsUnZip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!RootTools.isAccessGiven()) {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=eu.chainfire.supersu")));

        }

        // mk dir rommate
        File dir = new File(Environment.getExternalStorageDirectory() + "/rommate");
        if (!dir.exists()) {
            try {
                CommandCapture command = new CommandCapture(0, "mkdir " + Environment.getExternalStorageDirectory() + "/rommate");

                RootTools.getShell(true).add(command);
                while (!command.isFinished()) {
                    Thread.sleep(1);
                }

            } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                e.printStackTrace();
            }
        }

        // mk dir rommate/backups
        File dir1 = new File(Environment.getExternalStorageDirectory() + "/rommate/backups");
        if (!dir1.exists()) {
            try {
                CommandCapture command1 = new CommandCapture(0, "mkdir " + Environment.getExternalStorageDirectory() + "/rommate/backups");

                RootTools.getShell(true).add(command1);
                while (!command1.isFinished()) {
                    Thread.sleep(1);
                }

            } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                e.printStackTrace();
            }
        }

        // mk dir rommate/backups/fonts
        File dir2 = new File(Environment.getExternalStorageDirectory() + "/rommate/backups/fonts");
        if (!dir2.exists()) {
            try {
                CommandCapture command2 = new CommandCapture(0,  "mkdir " + Environment.getExternalStorageDirectory() + "/rommate/backups/fonts");

                RootTools.getShell(true).add(command2);
                while (!command2.isFinished()) {
                    Thread.sleep(1);
                }

            } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                e.printStackTrace();
            }
        }

            // backup fonts
            File dir3 = new File(Environment.getExternalStorageDirectory() + "/rommate/backups/fonts/fonts");
            if (!dir3.exists()) {
            RootTools.remount("/system", "RW");

                Toast.makeText(InstallFonts.this, "copying fonts", Toast.LENGTH_LONG).show();

                CommandCapture command3 = new CommandCapture(0, "chmod 777 /system/fonts");
                try {
                    RootTools.getShell(true).add(command3);
                    while (!command3.isFinished()) {
                        Thread.sleep(1);
                    }

                } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                    e.printStackTrace();
                }

            RootTools.copyFile("/system/fonts", Environment.getExternalStorageDirectory() + "/rommate/backups/fonts/", true, true);


        }

       // new InstallFont().execute();

    } // ends onCreate



    private class InstallFont extends AsyncTask<Void, Void, Void> {


        ProgressDialog progressInstallfonts;


        protected void onPreExecute() {

            progressInstallfonts = ProgressDialog.show(InstallFonts.this, "Install Fonts",
                    "installing...", true);

        }

        @Override
        protected Void doInBackground(Void... params) {

            String startpath = Environment.getExternalStorageDirectory() +  "/Download/fonts.zip";
            String finishpath = Environment.getExternalStorageDirectory() +  "/test1";
            String temp = getApplicationInfo().dataDir + "/orig/temp";

            //IF SZP IS LESS THAN 1 CHAR DO THIS.
            if (startpath.length() <= 1) {

                Toast.makeText(InstallFonts.this, "Install a compatible .zip", Toast.LENGTH_LONG).show();

                finish();
            } else {

                RootTools.remount("/system", "RW");
                // CREATES bootanim dir
                File dir3 = new File(temp);
                if (!dir3.exists() && !dir3.isDirectory()) {

                    CommandCapture command3 = new CommandCapture(0, "chmod 755 " + temp);
                    try {
                        RootTools.getShell(true).add(command3);
                        while (!command3.isFinished()) {
                            Thread.sleep(1);
                        }

                    } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                        e.printStackTrace();
                    }
                }

                CommandCapture command4 = new CommandCapture(0, "mkdir " + finishpath, "chmod 666 -R " + temp, "chmod 666 -R " + getApplicationInfo().dataDir + "/orig/bootanim");
                try {
                    RootTools.getShell(true).add(command4);
                    while (!command4.isFinished()) {
                        Thread.sleep(2);
                    }

                } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                    e.printStackTrace();
                }

                try {
                    unzip(startpath, finishpath + "/fonts/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
               // RootTools.copyFile(finishpath + "/fonts/", temp + "/", true, true);
                RootCommands.moveCopyRoot(finishpath + "/fonts/", temp + "/");

                CommandCapture command5 = new CommandCapture(0, "chmod -R 644 " + temp);
                try {
                    RootTools.getShell(true).add(command5);
                    while (!command5.isFinished()) {
                        Thread.sleep(1);
                    }

                RootTools.remount("/system", "RW");

// CHANGE PERMISSIONS TO COPY FINAL /VENDOR/OVERLAY FOLDER & FILES TO 777
                CommandCapture command8 = new CommandCapture(0, "chmod -R 777 /system/fonts");
                RootTools.getShell(true).add(command8);
                while (!command8.isFinished()) {
                    Thread.sleep(1);
                }

                // COPY NEW FILES TO /VENDOR/OVERLAY FOLDER
                RootCommands.moveCopyRoot(temp + "/fonts/", "/system");

                // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER & FILES TO 666 RECURING
                CommandCapture command9 = new CommandCapture(0, "chmod -R 644 /system/fonts");
                RootTools.getShell(true).add(command9);
                while (!command9.isFinished()) {
                    Thread.sleep(1);
                }

                // CHANGE PERMISSIONS OF FINAL /VENDOR/OVERLAY FOLDER BACK TO 777
                CommandCapture command10 = new CommandCapture(0, "chmod 755 /system/fonts");
                RootTools.getShell(true).add(command10);
                while (!command10.isFinished()) {
                    Thread.sleep(1);
                    RootTools.remount("/system", "RO");
                }

                RootCommands.DeleteFileRoot(finishpath);

                // CLOSE ALL SHELLS
                RootTools.closeAllShells();

                } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                    e.printStackTrace();
                }

            }

            finish();
            return null;
        }

        protected void onPostExecute(Void result) {

            progressInstallfonts.dismiss();

            finish();

            // LAUNCH LAYERS.CLASS
            overridePendingTransition(R.anim.back2, R.anim.back1);
            Intent iIntent = new Intent(InstallFonts.this, MainActivity.class);
            iIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            iIntent.putExtra("ShowSnackbar", true);
            iIntent.putExtra("SnackbarText", "Installed selected BootAnimation");
            startActivity(iIntent);
        }


    }

    /**
     * **********************************************************************************************************
     * UNZIP UTIL
     * ************
     * Unzip a zip file.  Will overwrite existing files.
     *
     * @param zipFile  Full path of the zip file you'd like to unzip.
     * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
     * @throws IOException *************************************************************************************************************
     */
    public void unzip(String zipFile, String location) throws IOException {

        int size;
        byte[] buffer = new byte[1024];

        try {

            if (!location.endsWith("/")) {
                location += "/";
            }
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024));
            try {
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {

                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if (null != parentDir) {
                            if (!parentDir.isDirectory()) {
                                parentDir.mkdirs();
                            }
                        }
                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, 1024);
                        try {
                            while ((size = zin.read(buffer, 0, 1024)) != -1) {
                                fout.write(buffer, 0, size);
                            }
                            zin.closeEntry();
                        } finally {
                            fout.flush();
                            fout.close();
                            out.close();
                        }
                    }
                }
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }


}
