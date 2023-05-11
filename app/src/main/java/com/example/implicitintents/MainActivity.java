package com.example.implicitintents;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> mARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            });

    private ActivityResultLauncher<Void> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(),
            new ActivityResultCallback<Bitmap>() {
                @Override
                public void onActivityResult(Bitmap result) {
                    imageView.setImageBitmap(result);
                }
            });
    private static final int CAMERA_REQUEST_CODE = 111;
    private EditText edUrl;
    private ImageView imageView;
    private Button btnUrl, btnCaptureImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        eventHandler();
    }
    private void initUI() {
        edUrl = findViewById(R.id.ed_url);
        btnUrl = findViewById(R.id.btn_open_url);
        imageView = findViewById(R.id.image_view);
        btnCaptureImage = findViewById(R.id.btn_capture_image);
    }

    private void eventHandler() {
        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrlInBrowser(edUrl.getText().toString());
            }
        });
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }
    public void openUrlInBrowser(String url) {
        //Creating an Intent object.
        Intent webIntent = new Intent();
        //Setting the Action to VIEW.
        webIntent.setAction(Intent.ACTION_VIEW);
        //Making URI of the URL.
        Uri uri = Uri.parse(url);
        //Setting data to uri.
        webIntent.setData(uri);

        //if(webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);

    }

    public void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    public void updatedTakePicture() {
        mActivityResultLauncher.launch(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Bitmap imgBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imgBitmap);
            } else {
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        }
    }
}