import java.util.regex.Pattern;

/**
 * Contient les infos sur une route, ainsi que la logique permettant notamment de récupérer des paramètres dans l'URL, tels que /api/members/{memberId}
 */
public class RouteInfo {
    public String method;
    public String path;
    private RouteHandler handler;

    public RouteInfo(String method, String path, RouteHandler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    public boolean matches(String method, String requestPath) {
        return Pattern.compile(method).matcher(method).matches() && Pattern.compile(path).matcher(requestPath).matches();
    }

    public RouteHandler getHandler() {
        return handler;
    }
}