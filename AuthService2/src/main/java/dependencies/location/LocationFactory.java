package dependencies.location;

public class LocationFactory {
    public static Location createLocation(double latitude, double longitude) {
        return new Location(latitude, longitude);
    }

    public static Location createLocation(String address) {
        return new Location(address);
    }

    public static Location createLocation(double latitude, double longitude, String address) {
        return new Location(latitude, longitude, address);
    }
}
