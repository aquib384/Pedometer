package com.secondapp.pedometer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import static android.os.Environment.getExternalStorageDirectory;
import static com.secondapp.pedometer.R.id.*;

public class Signup extends AppCompatActivity {



    private static final String IMAGE_DIRECTORY_NAME = "Customer";
    EditText firstName,lastName,ages,heigh,weigh;
    private RadioGroup radioGroup;

    //for image path from camera
    File mediaFile;
    String imagePath = "";
    private Uri fileUri;
    public File f;
    File dir;

    TextView camera,gallery,cancel;

    //shared pref;


    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        radioGroup=findViewById(radio);
        ages=findViewById(age);
        heigh=findViewById(height);
        weigh=findViewById(weight);
        sessionManager = new SessionManager(getApplicationContext());
        /**
         * checking login status
         */
        if (sessionManager.isLoggedIn()){
            Intent intent=new Intent(Signup.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public void check(){
        if (firstName.getText().toString().isEmpty())
            firstName.setError("please enter first name");
        if (lastName.getText().toString().isEmpty())
            lastName.setError("please enter last name");
        if (ages.getText().toString().isEmpty())
            ages.setError("please enter age name");
        if (heigh.getText().toString().isEmpty())
            heigh.setError("please enter heigth name");
        if (weigh.getText().toString().isEmpty())
            weigh.setError("please enter weight name");

    }


    public void onSignUp(View view){
            check();

            String name=firstName.getText().toString()+" "+lastName.getText().toString();
            int selectedId=radioGroup.getCheckedRadioButtonId();
            if (selectedId==-1)  Toast.makeText(this,"Select Gender",Toast.LENGTH_SHORT).show();
        RadioButton radioButton = findViewById(selectedId);
            try {

                String gender= radioButton.getText().toString();
                String age= ages.getText().toString();
                String height= heigh.getText().toString();
                String weight= weigh.getText().toString();

                sessionManager.createLoginSession(name,age,gender,height,weight,imagePath);
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    public void onUpload(View view){
        selectImage(this);

    }


    private void selectImage(Context context) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        camera=dialogView.findViewById(R.id.camera);
        gallery=dialogView.findViewById(R.id.gallery);
        cancel=dialogView.findViewById(R.id.cancel);

      camera.setOnClickListener(v -> {
          alertDialog.dismiss();
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          if (Build.VERSION.SDK_INT > 20) {
              File file = getCameraFile();
              imagePath = file.getPath();
              fileUri = FileProvider.getUriForFile(Signup.this, getApplicationContext().getPackageName() + ".provider", file);
          } else {
              fileUri = Uri.fromFile(getOutputMediaFile());
          }


          intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
          intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
          startActivityForResult(intent, 0);

      });

        gallery.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);

        });

        cancel.setOnClickListener(v -> {

            alertDialog.dismiss();
        });
        //
    }




    public File getCameraFile() {
        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());
        return new File(dir, "IMG_" + "" + timeStamp + ".jpg");
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(getExternalStorageDirectory().getPath(), IMAGE_DIRECTORY_NAME);
        f = mediaStorageDir;
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + "," + timeStamp + ".jpg");
        imagePath = mediaFile.getAbsolutePath();
        return mediaFile;
        //return mediaStorageDir;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Toast.makeText(this,"Picture uploaded Successfully",Toast.LENGTH_SHORT).show();

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                 imagePath = cursor.getString(columnIndex);
                               // imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }



}