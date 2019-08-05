package com.cuiyn.bookpush.tools;

public class BCryptPasswordEncoder {
    public String encode(String s) {
        s = BCrypt.hashpw(s, BCrypt.gensalt());
        return s;
    }
}
