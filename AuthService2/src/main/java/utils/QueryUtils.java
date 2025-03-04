package utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class QueryUtils {

    public static Map<String, String> parseQuery(String query) {
        Map<String, String> res = new HashMap<String, String>();

        if (query != null) {
            String[] pairs = query.split("&");

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : null; // Verify if value exists
                res.put(key, value);
            }
        }

        return res;
    }

    public static Map<String, String> parseJsonBody(InputStream inputStream) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder body = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        if (body.isEmpty()) {
            throw new IOException("Request body is empty");
        }
        JSONObject json = new JSONObject(body.toString());
        Map<String, String> res = new HashMap<>();

        for (String key : json.keySet()) {
            res.put(key, json.optString(key, null));
        }
        return res;
    }
}
