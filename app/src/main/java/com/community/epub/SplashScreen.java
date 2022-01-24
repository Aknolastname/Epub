package com.community.epub;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import static android.content.ContentValues.TAG;

public class SplashScreen extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 41;
    public static final ArrayList<BookItem> list = new ArrayList<>();
    boolean canRead;
    String path = Environment.getExternalStorageDirectory().getPath();

    FilenameFilter filter = new FilenameFilter()
    {
        @Override
        public boolean accept(File file, String name) {
            if (file.isDirectory() || name.endsWith(".epub")) {
                return true;
            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


//        this.makeDirectory("books");

        canRead = checkPermissionForReadExternalStorage();
        if(!canRead){
            try {
                requestPermissionForReadExternalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(canRead){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Ebooks...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            List<String> files = Arrays.asList(Environment.getExternalStorageDirectory().list(filter));

            Toast.makeText(this, path, Toast.LENGTH_LONG).show();
            for(String s: files){
                if(Files.isRegularFile(Paths.get(path+"/"+s)) && s.endsWith(".epub")){
//                    Toast.makeText(this, new File(path+"/"+s).getName(), Toast.LENGTH_SHORT).show();
                    try {
                        InputStream in = new FileInputStream(path+"/"+s);
                        BookItem item = new BookItem();
//                        copyBookFromAssetsToDevice(path+"/"+s, s);
                        item.setPath(path+"/"+s);
                        item.setBook((new EpubReader()).readEpub(in));
                        list.add(item);
                        in.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    list.add(path+"/"+s);
                }
                else if(Files.isDirectory(Paths.get(path+"/"+s))){
                    goUnder(path+"/"+s);
                }
            }
            progressDialog.dismiss();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    public boolean checkPermissionForReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int result1 = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExternalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            this.canRead = true;
            startActivity(new Intent(this, SplashScreen.class));
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void goUnder(String path1){
        String[] files = new File(path1).list(filter);
        if(files!=null && files.length>0){
            for (String s : files) {
                if (Files.isRegularFile(Paths.get(path1 + "/" + s)) && s.endsWith(".epub")) {
//                    Toast.makeText(this, new File(path1).getName(), Toast.LENGTH_SHORT).show();
                    try {
                        InputStream in = new FileInputStream(path1+"/"+s);
                        BookItem item = new BookItem();
//                        copyBookFromAssetsToDevice(path1+"/"+s, s);
                        item.setPath(path1+"/"+s);
                        item.setBook((new EpubReader()).readEpub(in));
                        list.add(item);
                        in.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    list.add(path1+"/"+s);
                } else if (Files.isDirectory(Paths.get(path1 + "/" + s))) {
                    goUnder(path1 + "/" + s);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void copyBookFromAssetsToDevice(String path, String fileName) {
        try
        {
            if (Files.exists(Paths.get(getStorageDirectory()+"/books/"+fileName))) return;
            InputStream localInputStream = new FileInputStream(path);
            FileOutputStream localFileOutputStream = new FileOutputStream(this.getStorageDirectory() + "/books/"+fileName);

            byte[] arrayOfByte = new byte[1024];
            int offset;
            while ((offset = localInputStream.read(arrayOfByte))>0)
            {
                localFileOutputStream.write(arrayOfByte, 0, offset);
            }
            localFileOutputStream.close();
            localInputStream.close();
//            Log.d(TAG, fileName+" copied to phone");
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
//            Log.d(TAG, "failed to copy");
            return;
        }
    }

    public boolean makeDirectory(String dirName) {
        boolean res;
        String filePath = new String(this.getStorageDirectory() + "/"+dirName);
        File file = new File(filePath);
        if (!file.exists()) {
            res = file.mkdirs();
        }else {
            res = false;
        }
        return res;
    }

    void check() {
        String path = this.getStorageDirectory() + "/books/"+"Alice.epub";
        File file = new File(path);
        if (file.exists()) {
            Log.w(TAG,"File installed");
        }else {
            Log.w(TAG,"File not installed");
        }
    }

    public String getStorageDirectory() {
        String res = "";
        res = getFilesDir().getAbsolutePath()+"/"+getString(this.getApplicationInfo().labelRes);
        return res;
    }
}