package com.example.firebasechat.view.main

import android.content.Intent


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebasechat.R
import com.example.firebasechat.databinding.ActivityMainBinding
import com.example.firebasechat.repository.SessionRepository
import com.example.firebasechat.view.base.BaseActivity
import com.example.firebasechat.view.chat.ChatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Login with Google Account.
 */
class MainActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    val TAG = "MainActivity"

    val RC_SIGN_IN = 1000

    lateinit var mBinding: ActivityMainBinding

    lateinit var mGoogleApiClient: GoogleApiClient

    val mAuthListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser == null) {
            Log.d("FirebaseAuth", "signed_out")
        } else {
            startChatRoom()
        }
    }

    val gso: GoogleSignInOptions by lazy { GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initGoogleApiClient()
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener)
    }

    fun setupViews() {
        setSupportActionBar(mBinding.toolbar)
        mBinding.loginButton.setOnClickListener { signIn() }
    }

    fun initGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                firebaseAuthWithGoogle(result.signInAccount!!)
            } else {
                Toast.makeText(this, "Google Sign In failed,", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback{
            Toast.makeText(this, "Google sign out.", Toast.LENGTH_SHORT).show()
        }
    }

    fun revokeAccess() {
        FirebaseAuth.getInstance().signOut()
        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback{
            Toast.makeText(this, "Google sign out.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionFailed(cr: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed:" + cr)
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    fun startChatRoom() {
        if (!SessionRepository.isLogin) return
        val roomId = 0
        ChatActivity.start(this, roomId)
        finish()
    }

}
