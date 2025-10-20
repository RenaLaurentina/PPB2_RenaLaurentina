package com.example.ppb2_rena

import android.content.Intent
import android.credentials.Credential
import android.media.session.MediaSession.Token
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.ppb2_rena.databinding.ActivityMainBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        credentialManager = CredentialManager.create(this)
        auth = Firebase.auth

        registerEvents()
    }

    fun registerEvents(){
        activityBinding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                val request = prepareRequest()
                loginByGoogle(request)
            }
        }
    }

    fun prepareRequest(): GetCredentialRequest {
        val serverClientid =
            "12121165611-9qakfej5quclskkdqfqkbmeba1l4ovnu.apps.googleusercontent.com"

        val googleOption = GetGoogleIdOption
            .Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientid)
            .build()

        val request = GetCredentialRequest
            .Builder()
            .addCredentialOption(googleOption)
            .build()
        return request
    }

    suspend fun loginByGoogle(request: GetCredentialRequest) {
        try {
            val result = credentialManager.getCredential(
                context = this,
                request = request
            )

            val credential = result.credential
            val idToken = GoogleIdTokenCredential.createFrom(credential.data)

            firebaseLoginCallback(idToken.idToken)

        } catch (exc: NoCredentialException) {
            Toast.makeText(this, "Login gagal :" + exc.message, Toast.LENGTH_LONG).show()
        } catch (exc: Exception) {
            Toast.makeText(this, "Login gagal :" + exc.message, Toast.LENGTH_LONG).show()
        }
    }

    fun firebaseLoginCallback(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_LONG).show()
                toTodoPage()
            } else {
                Toast.makeText(this, "Login gagal", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun onStart() {
        super.onStart()
        if (isAuthenticated()) {
            toTodoPage()
        }
    }

    private fun toTodoPage() {
        val intent = Intent(this, TaxtActivity::class.java)
        startActivity(intent)
        finish()
    }
}