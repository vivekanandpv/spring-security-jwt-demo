package com.example.springsecurityjwtdemo.utils;

import java.security.SecureRandom;

public class StaticDataProvider {
    public static String getRandomResetKey(int length) {
        String AB = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( length );
        for( int i = 0; i < length; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static final int MAX_LOGIN_ATTEMPTS = 5;
}
