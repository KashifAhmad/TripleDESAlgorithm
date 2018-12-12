package com.kashif.textencryptioncs;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Created by Kashif Ahmad
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //encryption type
    public static final String EncryptionScheme = "DESede";
    //text format
    private static final String TestUniCodeFormat = "UTF8";
    //
    byte[] arrayBytes;
    SecretKey key;
    @BindView(R.id.et_encryption_key)
    EditText etEncryptionKey;
    @BindView(R.id.et_text_to_encrypt)
    EditText etTextToEncrypt;
    @BindView(R.id.btn_encrypt)
    Button btnEncrypt;
    @BindView(R.id.tv_encryption_out)
    TextView tvEncryptionOutPut;
    @BindView(R.id.et_encrypted_text)
    EditText etEncryptedText;
    @BindView(R.id.btn_decrypt)
    Button btnDecrypt;
    @BindView(R.id.tv_decrypted_output)
    TextView tvDecryptionOut;
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    private String myEncryptionKey, myEncryptionScheme;
    private String strEncrytion, strDecryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnEncrypt.setOnClickListener(this);
        btnDecrypt.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_encrypt:
                if (etEncryptionKey.getText().length() >= 24) {
                    if (etTextToEncrypt.getText().toString().length() > 1) {
                        initEncryption();
                        strEncrytion = encrypt(etTextToEncrypt.getText().toString());
                        tvEncryptionOutPut.setText(strEncrytion);
                        etEncryptedText.setText(strEncrytion);
                    } else {
                        etTextToEncrypt.setError("enter text");
                    }
                } else {
                    etEncryptionKey.setError("key should be greater than 24");
                }
                break;
            case R.id.btn_decrypt:
                if (etEncryptionKey.getText().length() >= 24) {
                    initEncryption();
                    strDecryption = decrypt(etEncryptedText.getText().toString());
                    tvDecryptionOut.setText(strDecryption);

                } else {
                    etEncryptionKey.setError("key should be greater than 24");
                }
                break;
        }
    }
    private void initEncryption() {
        //getting encryption
        myEncryptionKey = etEncryptionKey.getText().toString();
        myEncryptionScheme = EncryptionScheme;
        try {
            Toast.makeText(this, "Triple DES encrypted", Toast.LENGTH_SHORT).show();
            arrayBytes = myEncryptionKey.getBytes(TestUniCodeFormat);
            ks = new DESedeKeySpec(arrayBytes);
            skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = skf.generateSecret(ks);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(TestUniCodeFormat);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encodeToString(encryptedText, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }


    public String decrypt(String encryptedString) {
        String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(encryptedString, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

}