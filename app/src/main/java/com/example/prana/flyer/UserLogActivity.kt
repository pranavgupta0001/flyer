package com.example.prana.flyer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import android.support.design.widget.Snackbar
import android.support.annotation.NonNull
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentActivity
import android.text.Layout
import android.util.Log
import android.widget.ImageView
import android.widget.Switch
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*


class UserLogActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001
    private var mCallbackManager: CallbackManager? = null
    var buttonNum = 5
    var fStatus = false
    var namee: String? = null


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_user_log)

        //hided title bar

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //facebook log in
        mCallbackManager = CallbackManager.Factory.create();




        if (mAuth.currentUser != null) {
            logIn()
        }

    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    ///Sign
    ///////with
    ///////////email
    ////////////////password
    fun goClicked(view: View) {
        //Check if we can log in the user


        if (emailEditText?.text.toString().equals("") || passwordEditText?.text.toString().equals("")) {
            guestClicked(view)
        } else {
            mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            logIn()


                        } else {
                            //Sign up the user
                            mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            //Add to database
                                            FirebaseDatabase.getInstance().getReference().child("users")
                                                    .child(task.result.user.uid)
                                                    .child("email").setValue(emailEditText?.text.toString())
                                            logIn()

                                        } else {
                                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()

                                        }
                                    }
                        }

                        // ...
                    }
        }
    }

    ///sign
    ///////with
    ///////////google


    fun googleSignInn(view: View) {
        buttonNum = 1
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        super.onActivityResult(requestCode, resultCode, data)

        fStatus = mCallbackManager?.onActivityResult(requestCode, resultCode, data) ?: false

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = mAuth.currentUser
                        namee = user?.displayName
                        logIn()
                    } else {
                        // If sign in fails, display a message to the user.
                        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    }

                    // ...
                }
    }


    ///Sign-in
    //////////with
    //////////////facebook
    // Initialize Facebook Login button

    fun facebookLogIn(view: View) {
        buttonNum = 2

        var loginButton = findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    @Override
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    override fun onCancel() {
                        Log.d("MainActivity", "Facebook onCancel.")
                        Toast.makeText(applicationContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }

                    override fun onError(error: FacebookException) {
                        if (!fStatus) {

                            logIn()
                        } else {
                            Toast.makeText(applicationContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                })


    }

    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = mAuth.getCurrentUser();
                        logIn()
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                }
    }


    ///guest
    ////////login
    fun guestClicked(view: View) {
        //Move to next Activity
        buttonNum = 3
        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
        var intent = Intent(this, EntryNavActivity::class.java)
        startActivity(intent)
    }

    fun logIn() {
        //Move to next Activity
        var intent = Intent(this, EntryNavActivity::class.java)
        var guest: String
        when (buttonNum) {
            1 -> guest = mAuth.currentUser?.displayName ?: String()
            2 -> guest = mAuth.currentUser?.displayName ?: String()
            else -> guest = "Guest"
        }
        Toast.makeText(this, "Welcome " + guest, Toast.LENGTH_SHORT).show()
        startActivity(intent)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mAuth.currentUser != null) {
            mAuth.signOut()
            LoginManager.getInstance().logOut();
            Toast.makeText(this, "Sign out Successful ", Toast.LENGTH_SHORT).show()
        }
    }
}