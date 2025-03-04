package api.utils;

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
}
