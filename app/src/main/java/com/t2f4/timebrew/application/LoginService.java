package com.t2f4.timebrew.application;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.jetbrains.annotations.NotNull;

public class LoginService {
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void signIn(String id, String pw,  com.google.android.gms.tasks.OnCompleteListener<AuthResult> onCompleteListener) {
        auth.signInWithEmailAndPassword(id, pw).addOnCompleteListener(onCompleteListener);
    }



}
