package shafir.irena.xmlandfirbase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    //properties:
    private FirebaseAuth mAuth;
    private GoogleApiClient mApiClient;  //Take away


    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.btnVerify)
    BootstrapButton btnVerify;
    @BindView(R.id.btnGoogle)
    SignInButton btnGoogle;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnRegister)
    BootstrapButton btnRegister;
    @BindView(R.id.btnLogin)
    BootstrapButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.enableAutoManage(this/* Activity for OnPause/Resume...*/,this);
        // "enableAutoManage" - helps to connect / disconnect when needed

        // Configure Google Sign In
                 GoogleSignInOptions gso = new GoogleSignInOptions
                         .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        builder.addApi(Auth.GOOGLE_SIGN_IN_API,gso);
        mApiClient = builder.build();

//        BootstrapText text = new BootstrapText.Builder(this).addText("hello").build();
//        btnRegister.setBootstrapText(text);

        // homework - read about the video view & add
    }


    private String getEmail() {
        return etEmail.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }


    private boolean isEmailValid() {
        String email = getEmail();
        boolean isEmailValid = email.contains("@") && email.length() > 5;
//        Pattern emailAddress = Patterns.EMAIL_ADDRESS;
//        return emailAddress.matcher(email).matches();
        if (!isEmailValid) {
            etEmail.setError("Invalid Email Address");
        } else etEmail.setError(null);
        return isEmailValid;
    }


    private boolean isPasswordValid() {
        String password = getPassword();
        boolean isPasswordValid = password.length() > 5;
        if (!isPasswordValid) {
            etPassword.setError("Password must contain at least 6 Charachters");
        } else etPassword.setError(null);
        return isPasswordValid;
    }


    @OnClick(R.id.btnRegister)
    public void register() {        // get the e mail
        if (validateForm()) return;
        showProgressBar(true);

        mAuth.createUserWithEmailAndPassword(getEmail(), getPassword())
                .addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }

    OnFailureListener onFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            showError(e);
        }
    };
    OnSuccessListener onSuccessListener = new OnSuccessListener<AuthResult>() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onSuccess(AuthResult authResult) {
//            if (mAuth.getCurrentUser() != null) {
//                if (!mAuth.getCurrentUser().isEmailVerified()) {
//                mAuth.getCurrentUser().sendEmailVerification();
//                }
//                else
//                    mAuth.getCurrentUser().reload();
//                gotoMain();

//            }
            showProgressBar(false);
            btnRegister.animate().alpha(0).
                    rotation(360).
                    setDuration(300).
                    withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            btnRegister.setVisibility(View.GONE);
                        }
                    });
            etPassword.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            etEmail.setVisibility(View.GONE);
            tilEmail.setVisibility(View.GONE);
            tilPassword.setVisibility(View.GONE);
            btnVerify.setVisibility(View.VISIBLE);
            btnVerify.animate().scaleX(2).scaleY(2);

        }
    };

    private void gotoMain() {
        showProgressBar(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // stops the user from getting back to the login from the next activity by pressing BACK
        startActivity(intent);
    }

    private ProgressDialog dialog;

    // a progress dialog contains:
    // Title, Massage, Icon & a progressBar
    private void showProgressBar(boolean show) {
        // lazy loading = not initialized in he on create/ start
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            // TODO: dismiss
            dialog.setMessage("Logging You In");
            dialog.setTitle("Connecting...");
        }
        if (show)
            dialog.show();
        else
            dialog.dismiss();
    }

    private void showError(@NonNull Exception e) {
        showProgressBar(false);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        Snackbar.make(etEmail, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", listener).show();
    }


    @OnClick(R.id.btnLogin)
    public void login() {
        if (validateForm()) return;
        showProgressBar(true);
        mAuth.getInstance().signInWithEmailAndPassword(getEmail(), getPassword())
                .addOnFailureListener(onFailureListener).addOnSuccessListener(onSuccessListener);
    }

    private boolean validateForm() {
        String email = getEmail();
        String password = getPassword();
        if (!isEmailValid() & !isPasswordValid()) {
            return true;
        }
        return false;
    }


    boolean sent = false;

    @OnClick(R.id.btnVerify)
    public void onViewClicked() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (!sent) {
            if (user == null) {
                return;
            } else
                user.sendEmailVerification();
            Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
            sent = true;

            btnVerify.setText("Refresh");
        } else
            user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (user.isEmailVerified()) {
                        gotoMain();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "pls try to connect again", Toast.LENGTH_SHORT).show();
                }
            });
    }


    @OnClick(R.id.btnGoogle)
    public void onGoogleClicked() {
        // intent .... GoogleApiClient
        //startActivityForResult
        Intent gsIntent = Auth.GoogleSignInApi.getSignInIntent(mApiClient);
        startActivityForResult(gsIntent, RC_SIGN_IN);
    }

    //Take Away
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                //google account
                GoogleSignInAccount account = result.getSignInAccount();

                // fireBase credentials
                AuthCredential authCredentials = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                //send result to fireBase
                mAuth.signInWithCredential(authCredentials).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                   gotoMain();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError(e);
                    }
                });

            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

}
