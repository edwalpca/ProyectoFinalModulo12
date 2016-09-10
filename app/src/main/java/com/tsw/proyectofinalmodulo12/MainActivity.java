package com.tsw.proyectofinalmodulo12;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private CallbackManager cM;
    private LoginButton lB;

    private TextView nombreProfileFB,emailProfileFB;
    private ProfilePictureView profileImage;// para ver la imagen del perfil

    // Componente para visualizar el Banner de Google.
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Ad unit ID: ca-app-pub-7633520457423281/3526351713
        //ID de aplicación: ca-app-pub-7633520457423281~6619418913
        //ID del bloque de anuncios: ca-app-pub-7633520457423281/

        //inicializar el SDK de Facebook para poder utilizarlo
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        cM = CallbackManager.Factory.create();

        // KeyHash de mi aplicacion en FaceBook.
        getFbKeyHash("NfgDlIG7XWJzUZRUL+bZySrMX1Q=");

        setContentView(R.layout.activity_main);

        lB = (LoginButton)findViewById(R.id.login_facebook);

        // Utilizare los siguientes componenentes para mostrar los datos e info del perfil del usuario.
        nombreProfileFB = (TextView)findViewById(R.id.nombrePerfil);
        emailProfileFB  = (TextView)findViewById(R.id.emailPerfil);




        lB.registerCallback(cM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Profile profile = Profile.getCurrentProfile();

                nombreProfileFB.setText( profile.getName());
                profileImage = (ProfilePictureView) findViewById(R.id.profilePicture);
                profileImage.setProfileId(profile.getId());
                Toast.makeText(MainActivity.this, "¡Bienvenido! " + profile.getName() + " has iniciado sesion" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "¡Inicio de sesión cancelado!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "¡Inicio de sesión NO exitoso!", Toast.LENGTH_LONG).show();
            }

            // hago uso del AccessTokenTracker para ocultar la imagen del profile como el nombre.
            AccessTokenTracker accessTokenTracker= new AccessTokenTracker()
            {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                    if (newToken == null){
                        nombreProfileFB.setText("");
                        profileImage.setProfileId(null);
                    }
                }
            };




        });


        // Proceso para Crear el Banner Inferior.
        adView = (AdView) findViewById(R.id.banner_inferior_googleAds);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);


    }

    public void getFbKeyHash(String packageName){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //Log.d("KeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
               // System.out.println("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e){

        }catch (NoSuchAlgorithmException e){

        }

    }

    protected void onActivityResult(int reqCode, int resCode, Intent i){
        cM.onActivityResult(reqCode, resCode, i);
    }



    //Metodos para la seccion de Google Ads
    @Override
    protected void onPause() {
        if(adView != null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(adView != null){
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }


}
