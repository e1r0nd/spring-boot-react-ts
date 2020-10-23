package com.example.demo.utilities;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {
    private ErrorMessage() {
    }

    public static Map<String, String> errorMessage(int code, String msg) {
        HashMap<String, String> map = new HashMap<>();
        map.put("error", String.valueOf(code));
        map.put("message", msg);
        return map;
    }
}
