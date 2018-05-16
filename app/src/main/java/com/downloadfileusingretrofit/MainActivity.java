package com.downloadfileusingretrofit;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Url;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 200;

    private static final String TAG = "DownloadFileAsyncTask"   ;
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://bintray.com/tigervnc/stable/tigervnc/1.8.0")
            .build();
    static IMyService service = retrofit.create(IMyService.class);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Marshmallow+
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

            }, REQUEST_CODE_PERMISSION);

        } else {
            // Pre-Marshmallow
        }







    }


    public static void downloadService()

    {

        Log.d("mmm", "Call Download Service");

        service.getFile("https://bintray.com/tigervnc/stable/download_file?file_path=tigervnc64-1.8.0.exe").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                Log.d(TAG, response.message());
                if(!response.isSuccess()){
                    Log.e(TAG, "Something's gone wrong");
                    // TODO: show error message
                    return;
                }
                DownloadFileAsyncTask downloadFileAsyncTask = new DownloadFileAsyncTask();
                try {
                    downloadFileAsyncTask.execute(response.body().byteStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());

            }


        });

    }



    private static class  DownloadFileAsyncTask extends AsyncTask<InputStream, Void, Boolean> {

        final String appDirectoryName = "QDSS";
        final File imageRoot = new File(Environment.getExternalStorageDirectory(), appDirectoryName);
        final String filename = "tigervnc64-1.8.0.exe";

        @Override
        protected Boolean doInBackground(InputStream... params) {
            InputStream inputStream = params[0];
            File file = new File(imageRoot, filename);
            OutputStream output = null;
            try {
                output = new FileOutputStream(file);

                byte[] buffer = new byte[1024]; // or other buffer size
                int read;

                Log.d(TAG, "Attempting to write to: " + imageRoot + "/" + filename);
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    Log.v(TAG, "Writing to buffer to output stream.");
                }
                Log.d(TAG, "Flushing output stream.");
                output.flush();
                Log.d(TAG, "Output flushed.");
            } catch (IOException e) {
                Log.e(TAG, "IO Exception: " + e.getMessage());
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (output != null) {
                        output.close();
                        Log.d(TAG, "Output stream closed sucessfully.");
                    }
                    else{
                        Log.d(TAG, "Output stream is null");
                    }
                } catch (IOException e){
                    Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            Log.d(TAG, "Download success: " + result);
            // TODO: show a snackbar or a toast
        }
    }



}
