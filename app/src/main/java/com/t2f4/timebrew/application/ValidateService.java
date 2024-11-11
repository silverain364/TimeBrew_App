package com.t2f4.timebrew.application;

import android.util.Patterns;

public class ValidateService {
    public boolean validateEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validatePhone(String phone){
        return Patterns.PHONE.matcher(phone).matches();
    }

}
