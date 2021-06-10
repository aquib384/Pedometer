package com.secondapp.pedometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Signup extends AppCompatActivity {


    private static final String IMAGEPATH = "";
    private static final String IMAGESYNC = "";
    private static final String IMAGE_DIRECTORY_NAME = "Customer";
    EditText firstName,lastName,ages,heigh,weigh;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button signup,upload;

    File mediaFile;
    String imagePath = "";
    private Uri fileUri;
    public File f;
    File dir;


    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    //shared pref;

    public static final String MyPREFERENCES = "MyData" ;
    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String SEX = "sex";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "height";
    public static final String IMAGE_PATH = "image";



    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        firstName=findViewById(R.id.firstname);
        lastName=findViewById(R.id.lastname);
        radioGroup=findViewById(R.id.radio);
        ages=findViewById(R.id.age);
        heigh=findViewById(R.id.height);
        weigh=findViewById(R.id.weight);
        signup=findViewById(R.id.signup);
        upload=findViewById(R.id.upload);

        preferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //initialization;
        if (isSignedUp()){
            Intent intent =new Intent(Signup.this,MainActivity.class);
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
    public void onClick(View view){
        check();


        String name=firstName.getText().toString()+" "+lastName.getText().toString();

            int selectedId=radioGroup.getCheckedRadioButtonId();
            if (selectedId==-1)  Toast.makeText(this,"Select Gender",Toast.LENGTH_SHORT).show();
            radioButton=findViewById(selectedId);
            String gender=radioButton.getText().toString();
            String age= ages.getText().toString();
            String height= heigh.getText().toString();
            String weight= weigh.getText().toString();

            SharedPreferences.Editor editor=preferences.edit();
            editor.putString(NAME,name);
            editor.putString(AGE,age);
            editor.putString(SEX,gender);
            editor.putString(HEIGHT,height);
            editor.putString(WEIGHT,weight);
            editor.putString(IMAGE_PATH,imagePath);
            editor.putBoolean("signedup", Boolean.parseBoolean("true"));
            editor.apply();
            Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(Signup.this,MainActivity.class);
            startActivity(intent);

    }
    public boolean isSignedUp(){
        boolean signedin=preferences.getBoolean("signedup", Boolean.parseBoolean("false"));

        return signedin;

    }

    public void onUpload(View view){
        selectImage(this);

    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT > 20) {
                        File file = getCameraFile();
                        imagePath = file.getPath();
                        fileUri = FileProvider.getUriForFile(Signup.this, getApplicationContext().getPackageName() + ".provider", file);
                    } else {
                        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public File getCameraFile() {
        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());
        return new File(dir, "IMG_" + "" + timeStamp + ".jpg");
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), IMAGE_DIRECTORY_NAME);
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

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + "," + timeStamp + ".jpg");
        } else {
            return null;
        }
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


                      //  imageView.setImageBitmap(selectedImage);

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