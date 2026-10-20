package be.thomasmore.nudibranch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;
    @NotBlank
    private String locationCity;
    @NotBlank
    private String locationCountry;
    private String locationDivespot;
    @NotBlank
    private Double latitude;
    @NotBlank
    private Double longitude;
    @OneToMany(mappedBy = "location")
    private Collection<Sighting> sighting;


    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationDivespot() {
        return locationDivespot;
    }

    public void setLocationDivespot(String locationDivespot) {
        this.locationDivespot = locationDivespot;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public Collection<Sighting> getSighting() {
        return sighting;
    }

    public void setSighting(Collection<Sighting> sighting) {
        this.sighting = sighting;
    }
}
