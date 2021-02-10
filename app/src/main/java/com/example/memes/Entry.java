package com.example.memes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class Entry extends AppCompatActivity {
    private static final int RC_SIGN_IN = 120;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private GoogleSignInClient googleSignInClient;

    //элементы в активити
    private EditText Elogin;
    private EditText Epassword;


    private Button SignIn;
    private Button newAcc;
    private Button googleSignIn;
    private Button vkAuth;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //убрать верхнюю полоску с временем и зарядкой
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_entry);

        //кнопки и едиты
        init();

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        //нажатия на кнопочки
        clickers();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
        } else {
        }
    }

    private void init() {
        //иницализация кнопок с layout
        Elogin = (EditText) findViewById(R.id.editLogin);
        Epassword = (EditText) findViewById(R.id.editPassword);


        SignIn = (Button) findViewById(R.id.signIn);
        newAcc = (Button) findViewById(R.id.newAcc);
        googleSignIn = (Button) findViewById(R.id.googleSighIn);
        vkAuth = (Button) findViewById(R.id.vkAuth);
    }

    private void clickers () {
        //обработка нажатий
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signing(Elogin.getText().toString(), Epassword.getText().toString());
            }
        });
        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Entry.this, Registration.class);
                startActivity(intent);
            }
        });
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        vkAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VK.login(Entry.this, Arrays.asList(VKScope.EMAIL, VKScope.WALL));
            }
        });
    }

    //вход через почту
    public void signing(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Entry.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Entry.this, Profile.class);
                    startActivity(intent);
                } else
                    Toast.makeText(Entry.this, "Авторизация провалена", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //вход через гугл
    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        //добавить в базу данных.
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount!=null) {
            String name = googleSignInAccount.getDisplayName();
            String username = googleSignInAccount.getId();
            String email = googleSignInAccount.getEmail();
            createDataBaseUser(name, username, email);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            //гугл
            case RC_SIGN_IN:
                super.onActivityResult(requestCode, resultCode, data);
                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                if (requestCode == RC_SIGN_IN) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d("yes", "firebaseAuthWithGoogle:" + account.getId());
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Log.w("no", "Google sign in failed", e);
                        // ...
                    }

                }
                break;
            default:
//                вк
                VKAuthCallback callback = new VKAuthCallback() {
                    @Override
                    public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                        try {
                            //РАЗОБРАТЬСЯ С ВК АПИ И СДЕЛАТЬ ПО ЧЕЛОВЕЧЕСКИ !!!
                            Log.d("vkid", String.valueOf(vkAccessToken.getUserId()));
                            //авторазиция через вк с помощью почты
                            //userId+@vk.auth - почта
                            //UserId - пароль
                            firebaseAuthwithVk(String.valueOf(vkAccessToken.getUserId())+"@vk.auth", String.valueOf(vkAccessToken.getUserId()));

                            String name = String.valueOf(vkAccessToken.getUserId());
                            String username = String.valueOf(vkAccessToken.getUserId());
                            String email = String.valueOf(vkAccessToken.getUserId())+"@vk.auth";

                            createDataBaseUser(name, username, email);

                        } catch (Exception e) {
                        }

                    }

                    @Override
                    public void onLoginFailed(int i) {
                        Toast.makeText(Entry.this, "Error", Toast.LENGTH_SHORT);
                    }
                };
                if (data == null || !VK.onActivityResult(requestCode, resultCode, data, (VKAuthCallback) callback)) {
                    Toast.makeText(Entry.this, "Error2", Toast.LENGTH_SHORT);
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("yes", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Entry.this, Profile.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("no", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Entry.this, "smth went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthwithVk(String email, String password) {
        //попробовать войти, если акк не сущесвует, создать и войти.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent= new Intent (Entry.this, Profile.class);
                    startActivity(intent);
                }
                else {
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(Entry.this, Profile.class);
                                    startActivity(intent);
                                }
                                else Toast.makeText(Entry.this, "smth gone wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //попытка через customToken
        /*
//        Log.d("vkAuthToken", AccessToken);
//        String customToken = FirebaseAuth.getInstance().createCustomToken(AccessToken);
        String token = FirebaseAuth.getInstance().creatCustomToken();
        mAuth.signInWithCustomToken(customToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("yes", "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent =  new Intent(Entry.this, Profile.class);
                            startActivity(intent);
                            Toast.makeText(Entry.this, "Good", Toast.LENGTH_SHORT);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("vkAuth", "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(Entry.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }); */
    }

    //прооверка, существует ли такой пользователь в бд
    // если нет, создать.
    private void createDataBaseUser (String name, String username, String email) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.child(username).exists()) {
                    UsersHelperClass googleuser = new UsersHelperClass(name, username, email);
//            myRef.push().setValue(googleuser);
                    myRef.child(username).setValue(googleuser);
                }else{
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}