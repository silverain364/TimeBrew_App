package com.t2f4.timebrew.application;

import android.util.Patterns;

public class ValidateService {
    public boolean validateEmail(String email){
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else{
            return true;
        }
    }
}
