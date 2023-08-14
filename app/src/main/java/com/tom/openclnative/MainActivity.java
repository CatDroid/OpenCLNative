package com.tom.openclnative;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private final int info[] = new int[3]; // Width, Height, Execution time (ms)
    private Bitmap bmpOrig, bmpOpenCL, bmpNativeC;
    private ImageView imageView;
    private TextView textView;
    private String kernelPath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageHere);
        textView = (TextView) findViewById(R.id.resultText);

        kernelPath = copyFile("bilateralKernel.cl");

        bmpOrig = BitmapFactory.decodeResource(this.getResources(), R.drawable.brusigablommor);
        info[0] = bmpOrig.getWidth();
        info[1] = bmpOrig.getHeight();

        bmpOpenCL = Bitmap.createBitmap(info[0], info[1], Bitmap.Config.ARGB_8888);
        bmpNativeC = Bitmap.createBitmap(info[0], info[1], Bitmap.Config.ARGB_8888);

        textView.setText("Original");
        imageView.setImageBitmap(bmpOrig);

    }

    public void showOriginalImage(View v)
    {
        textView.setText("Original");
        imageView.setImageBitmap(bmpOrig);
    }

    public void showOpenCLImage(View v)
    {
        runOpenCL(kernelPath, bmpOrig, bmpOpenCL, info);
        textView.setText("Bilateral Filter, OpenCL, Processing time is " + info[2] + " ms");
        imageView.setImageBitmap(bmpOpenCL);
    }

    public void showNativeCImage(View v)
    {
        runNativeC(bmpOrig, bmpNativeC, info);
        textView.setText("Bilateral Filter, NativeC, Processing time is " + info[2] + " ms");
        imageView.setImageBitmap(bmpNativeC);
    }


    private String copyFile(final String f) {
        InputStream in;
        try {
            in = getAssets().open(f);
            final File of = new File(getExternalCacheDir().getAbsolutePath() , f);

            final OutputStream out = new FileOutputStream(of);

            final byte b[] = new byte[65535];
            int sz = 0;
            while ((sz = in.read(b)) > 0) {
                out.write(b, 0, sz);
            }
            in.close();
            out.close();
            return of.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public native String stringFromJNI();

    public static native int runOpenCL(String kernelPath, Bitmap bmpIn, Bitmap bmpOut, int info[]);
    public static native int runNativeC(Bitmap bmpIn, Bitmap bmpOut, int info[]);

}