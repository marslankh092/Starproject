package com.example.starproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.OctogonImageView;
import com.github.siyamed.shapeimageview.StarImageView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.github.florent37.shapeofview.shapes.StarView;

public class MainActivity extends AppCompatActivity {

    Button imgPickImageStar, imgPickImagePolygon,saveImage;
    int pickImageInt = 1;
    private InterstitialAd mInterstitialAd;
    private StarImageView starImage;
    private OctogonImageView polugon;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    int imagePicker = 1;

    //MobileAds.initialize(this, APP_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        star = findViewById(R.id.star1V);
        starImage = findViewById(R.id.starImage);
        polugon = findViewById(R.id.polygonimage);

        imgPickImageStar = (Button) findViewById(R.id.image1);
        imgPickImagePolygon = (Button) findViewById(R.id.image2);
        saveImage= (Button) findViewById(R.id.image3);

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.blackimage);
        starImage.setImageDrawable(new BitmapDrawable(getResources(), image));

        polugon.setImageDrawable(new BitmapDrawable(getResources(), image));

//        starImage.setImageDrawable(image);
//        starImage = findViewById(R.id.starImhg);
//        setStarImageView= findViewById(R.id.test)


        //code 2
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdView adView = findViewById(R.id.adView);

                List<String> testDeviceIds = Arrays.asList("FBB39CDB455AA5740A492FD79AB25F09");
                RequestConfiguration configuration =
                        new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
                MobileAds.setRequestConfiguration(configuration);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);

            }
        });


        loadAd();


        imgPickImageStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker = 1;

                if (mInterstitialAd != null) {

                    mInterstitialAd.show(MainActivity.this);
                } else {
                    ImagePicker();
                }


            }
        });

        imgPickImagePolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker = 2;

                if (mInterstitialAd != null) {

                    mInterstitialAd.show(MainActivity.this);
                } else {
                    OpenCamera();
                }


            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();


            }
        });


//@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        if (requestCode == 1) {
//            final Bundle extras = data.getExtras();
//            if (extras != null) {
//                //Get image
//                Bitmap newProfilePic = extras.getParcelable("data");
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == pickImageInt) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                starImage.setImageBitmap(selectedImage);

                starImage.setImageDrawable(new BitmapDrawable(getResources(), selectedImage));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            polugon.setImageDrawable(new BitmapDrawable(getResources(), photo));
//            imageView.setImageBitmap(photo);
        } else {
            Toast.makeText(MainActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }


    public void loadAd() {
        List<String> testDeviceIds = Arrays.asList("FBB39CDB455AA5740A492FD79AB25F09");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getApplicationContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        //   Log.i(TAG, "onAdLoaded");

                        //add code
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {

                                if (imagePicker == 1) {
                                    ImagePicker();
                                } else {
                                    OpenCamera();
                                }
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                //  Log.e(TAG, "Ad failed to show fullscreen content.");
//                                mInterstitialAd = null;

                                if (imagePicker == 1) {
                                    ImagePicker();
                                } else {
                                    OpenCamera();
                                }

                            }

                            @Override
                            public void onAdImpression() {

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {


                            }
                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        //   Log.d(TAG, loadAdError.toString());
//                        mInterstitialAd = null;
                    }

                });


    }


    public void ImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageInt);
    }

    public void OpenCamera() {
        if (checkPermission()) {

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            requestPermission();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
        }
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_CAMERA_PERMISSION_CODE);
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg".replace(":","");

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(getApplicationContext(),"Image Saved in device.",Toast.LENGTH_LONG).show();

//            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}