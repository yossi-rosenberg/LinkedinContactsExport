package dataTypes;

public class UserDetails {
    private String name;
    private String placeOfWork;
    private String location;

    public UserDetails(String userName, String userPlaceOfWork, String userLocation) {
        name = userName;
        placeOfWork = userPlaceOfWork;
        location = userLocation;
    }

    public String getLocation() {
        return location;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

