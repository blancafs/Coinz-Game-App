package com.example.blanca.coinz2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private String tag = "AccountActivity";

    // Variables used for layout objects
    static TextView noSteps;
    static TextView noCoinsPi;
    static TextView noCoinsSen;
    static TextView plalevel;
    static TextView comlevel;
    static TextView myaccount;
    static ImageView editImage;
    static ImageView profileImage;
    private Button account2wallet;
    private Button account2map;
    private Switch modeSwitch;

    private static Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        player = MapActivity.player;

        Log.d(tag, "[onCreate] data " + player.getGold() +" " + player.getLevel()+ " " +player.getCommunityLevel()+"===============");

        // initiating textviews
        noSteps = findViewById(R.id.tvNoSteps);
        noCoinsPi = findViewById(R.id.tvNoCoinsPicked);
        noCoinsSen = findViewById(R.id.tvNoCoinsSent);
        plalevel = findViewById(R.id.tvPlayerLevel);
        comlevel = findViewById(R.id.tvComLevel);
        myaccount = findViewById(R.id.tvMyAccount);
        myaccount.setText(player.getEmail());
        editImage = findViewById(R.id.editProfileImage);
        editImage.setOnClickListener(this);
        profileImage = findViewById(R.id.profImage);
        account2wallet = findViewById(R.id.btaccount2wallet);
        account2wallet.setOnClickListener(this);
        account2map = findViewById(R.id.btaccount2map);
        account2map.setOnClickListener(this);

        // Setting switch
        modeSwitch = findViewById(R.id.modeSwitch);
        setUpSwitch();

        //noSteps.setText(noStepsWalked);
        noCoinsPi.setText(Integer.toString(player.getTotalCoins()) + " coins picked up so far!");
        noCoinsSen.setText(Integer.toString(player.getTotalSent()) + " coins sent so far!");
        noSteps.setText(Double.toString(Math.round(player.getTotalDistance())) + "m walked!");
        plalevel.setText(Integer.toString(player.getLevel()));
        comlevel.setText(Integer.toString(player.getCommunityLevel()));

        // get previously stored image and set it to that!
        String imagePath = MySharedPreferences.getPlayerImage(getApplicationContext(), player.getEmail());
        if(!(imagePath.length() < 1)){
            Log.d(tag, "[onCreate]: Saved image exists in memory, attempting to retrieve it");
            File f = new File(imagePath);
            Picasso.with(this).load(f).into( profileImage);
        }else{
            Log.d(tag,"[onCreate]: No saved image was found in memory");
        }
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        View toastLayout = inflater.inflate(R.layout.modetoast, findViewById(R.id.llCustom));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    private String getImageFilePath(String email){
        // make image bitmap
        BitmapDrawable draw = (BitmapDrawable) profileImage.getDrawable();
        Bitmap bitmap = draw.getBitmap();

        // Make directory for images if not there
        File dir = new File(getFilesDir() + "/images");
        dir.mkdirs();
        Log.d(tag, "[getImageFilePath]: directory created: "+ dir.getAbsolutePath());

        // Making filename
        String fileName = dir.listFiles().length + "image";
        Log.d(tag, "[getImageFilePath]: image name: "+ fileName);

        // Make file
        FileOutputStream outStream = null;
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        try {
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return path as string
        return outFile.getPath();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        //save image!
        MySharedPreferences.setPlayerImage(getApplicationContext(), getImageFilePath(player.getEmail()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editProfileImage:
                Log.d(tag, "[onClick] Button to edit profile image clicked.");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture!"), 1);
                break;
            case R.id.btaccount2wallet:
                Log.d(tag,"[onClick] Button to wallet has been clicked.");
                startActivity(new Intent(this,WalletActivity.class));
                break;
            case R.id.btaccount2map:
                Log.d(tag,"[onClick] Button to map page clicked.");
                startActivity(new Intent(this,MapActivity.class));
                break;
        }
    }

    // Handles the image chooser
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                //Get ImageURi and load with help of picasso
                Picasso.with(AccountActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit()
                        .into((ImageView) findViewById(R.id.profImage));

            }
        }
    }
    // Set up the switch functionality
    private void setUpSwitch(){
        Boolean mode = player.getCurrentMode();
        modeSwitch.setChecked(mode);
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                player.setCurrentMode(b);
            }
        });
    }
}