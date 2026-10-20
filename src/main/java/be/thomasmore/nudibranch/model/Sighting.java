package be.thomasmore.nudibranch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


@Entity
public class Sighting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sightingId;
    private String description;
    //NotNull want klasse - not blank kan enkel bij Strings!
    @NotNull(message = "Datum verplicht")
    private LocalDate dateOfSighting;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Locatie verplicht")
    private Location location;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Nudibranch verplicht")
    private Nudibranch nudibranch;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Vriend verplicht")
    private Friend friend;


    public Nudibranch getNudibranch() {
        return nudibranch;
    }

    public void setNudibranch(Nudibranch nudibranch) {
        this.nudibranch = nudibranch;
    }

    public Integer getSightingId() {
        return sightingId;
    }

    public void setSightingId(Integer sightingId) {
        this.sightingId = sightingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateOfSighting() {
        return dateOfSighting;
    }

    public void setDateOfSighting(LocalDate dateOfSighting) {
        this.dateOfSighting = dateOfSighting;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }
}