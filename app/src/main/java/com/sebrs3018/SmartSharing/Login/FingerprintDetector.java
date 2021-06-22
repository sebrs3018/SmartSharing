package com.sebrs3018.SmartSharing.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.User;
import com.sebrs3018.SmartSharing.Login.LoginActivity;
import com.sebrs3018.SmartSharing.Navigation_Activity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerprintDetector  {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Context context;
    private boolean success;
    private User user;

    public FingerprintDetector(User _user, Context _context){
        user = _user;
        context = _context;
        createKey();
        initPromp();
    }

    private void initPromp(){
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setNegativeButtonText("Annulla")
                .build();
    }

    private void createKey(){
        /* Creating a key */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            generateSecretKey(new KeyGenParameterSpec.Builder(
                    user.getUsername(),
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    // Invalidate the keys if the user has registered a new biometric
                    // credential, such as a new fingerprint. Can call this method only
                    // on Android 7.0 (API level 24) or higher. The variable
                    // "invalidatedByBiometricEnrollment" is true by default.
                    .setInvalidatedByBiometricEnrollment(true)
                    .build());
        }
    }

    public boolean startFingerPrintDetection(boolean isRegistration){

        executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt((FragmentActivity) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override       //Viene attivato quando si torna indietro col pulsante indietro dell'OS
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                context.startActivity(new Intent(context, LoginActivity.class));
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                /* Ritorno alla scherma di login */
                success = true;
                if(isRegistration){
                    Toast.makeText(context,
                            "Registrazione impronta avvenuto con successo!", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                else{
                    Toast.makeText(context, "Benvenuto " + user.getUsername() , Toast.LENGTH_SHORT).show();
                    SessionManager sm = new SessionManager(context);
                    sm.setUserSession(user.getUsername(), user.getAddress());

//                    sm.setUserSession();

                    context.startActivity(new Intent(context, Navigation_Activity.class));
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
                success = false;
            }
        });


        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
/*        Button biometricLoginButton = findViewById(R.id.biometric_login);
        biometricLoginButton.setOnClickListener(view -> {*/

            Cipher cipher = getCipher();
            SecretKey secretKey = getSecretKey();   //Se viene data una secretKey sbagliata...

            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
        return success;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            if (keyGenerator != null) {
                keyGenerator.init(keyGenParameterSpec);
            }
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        if (keyGenerator != null) {
            keyGenerator.generateKey();
        }
    }

    private SecretKey getSecretKey() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // Before the keystore can be accessed, it must be loaded.
        try {
            keyStore.load(null);
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }


        SecretKey secretKey = null;
        try {
            //Prendo key dato username
            secretKey = ((SecretKey)keyStore.getKey(user.getUsername(), null));   //Questa chiave è associata alla password (più registrazioni potrebbero comportare dei problemi)
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    private Cipher getCipher() {
        Cipher c = null;
        try {
            c =  Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return c;
    }


}
