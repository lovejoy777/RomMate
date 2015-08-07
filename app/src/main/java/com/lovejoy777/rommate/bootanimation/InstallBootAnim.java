package com.lovejoy777.rommate.bootanimation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
 * Created by lovejoy777 on 30/06/15.
 */
public class InstallBootAnim extends AppCompatActivity {

    static final String TAG = "InstallBootAnim";

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

        // mk dir rommate/backups/boots
        File dir2 = new File(Environment.getExternalStorageDirectory() + "/rommate/backups/boots");
        if (!dir2.exists()) {
            try {
                CommandCapture command2 = new CommandCapture(0,  "mkdir " + Environment.getExternalStorageDirectory() + "/rommate/backups/boots");

                RootTools.getShell(true).add(command2);
                while (!command2.isFinished()) {
                    Thread.sleep(1);
                }

            } catch (IOException | TimeoutException | InterruptedException | RootDeniedException e) {
                e.printStackTrace();
            }
        }

        // backup bootanimation
        File dir3 = new File(Environment.getExternalStorageDirectory() + "/rommate/backups/boots/bootanimation.zip");
        if (!dir3.exists()) {
            RootTools.remount("/system/media", "RW");

            RootTools.copyFile("/system/media/bootanimation.zip", Environment.getExternalStorageDirectory() + "/rommate/backups/boots/", true, true);

        }


        new InstallBootanim().execute();

    } // end oncreate

    private class InstallBootanim extends AsyncTask<Void, Void, Void> {


        ProgressDialog progressInstallbootanim;


        protected void onPreExecute() {

            progressInstallbootanim = ProgressDialog.show(InstallBootAnim.this, "install BootAnimation",
                    "installing...", true);

        }

        @Override
        protected Void doInBackground(Void... params) {

            // GET STRING SZP
            final Intent extras = getIntent();
            String SZP = null;
            if (extras != null) {
                SZP = extras.getStringExtra("key1");
            }


            try {
            CommandCapture command7 = new CommandCapture(0, "mkdir " + Environment.getExternalStorageDirectory() + "/temp");

                RootTools.getShell(true).add(command7);
                while (!command7.isFinished())
                    Thread.sleep(1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RootTools.remount("/system", "RW");
            try {
                unzip (SZP, Environment.getExternalStorageDirectory() + "/temp");
            } catch (IOException e) {
                e.printStackTrace();
            }



            RootTools.copyFile(Environment.getExternalStorageDirectory() + "/temp/bootanimation.zip", "/system/media/", true, true);

            RootTools.remount("/system/media", "RW");

            try {
            CommandCapture command5 = new CommandCapture(0, "chmod 644 /system/media/bootanimation.zip");

                   RootTools.getShell(true).add(command5);
                   while (!command5.isFinished()) {
                       Thread.sleep(1);
                   }

                RootCommands.DeleteFileRoot(Environment.getExternalStorageDirectory() + "/temp");

                        // CLOSE ALL SHELLS
                        RootTools.closeAllShells();
            } catch (IOException e) {
                e.printStackTrace();


              } catch (RootDeniedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            return null;
        }



        protected void onPostExecute(Void result) {

            progressInstallbootanim.dismiss();


            // LAUNCH MainActivity
            overridePendingTransition(R.anim.back2, R.anim.back1);
            Intent iIntent = new Intent(InstallBootAnim.this, MainActivity.class);
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
     * @throws java.io.IOException *************************************************************************************************************
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
