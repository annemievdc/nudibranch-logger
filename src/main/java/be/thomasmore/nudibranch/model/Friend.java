package be.thomasmore.nudibranch.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Collection;


@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer friendId;
    @NotBlank(message = "Naam is verplicht")
    private String friendName;
    @NotBlank(message = "Username is verplicht")
    private String friendUsername;
    @NotBlank(message = "Email is verplicht")
    private String friendEmail;
    private String friendImgUrl;
    private LocalDate friendDob;

    @OneToMany(mappedBy = "friend")
    private Collection<Sighting> sightings;

    public String getFullImgUrl() {
        // 1. Is er geen afbeelding ingevuld? Gebruik de default.
        if (friendImgUrl == null || friendImgUrl.isBlank()) {
            return "/img/friends/default-user.jpg";
        }

        // 2. Is het een Cloud upload? Gebruik de URL direct.
        if (friendImgUrl.startsWith("http")) {
            return friendImgUrl;
        }

        // 3. Is het een lokale afbeelding uit de database? Plak het pad ervoor.
        return "/img/friends/" + friendImgUrl;
    }
    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer id) {
        this.friendId = id;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String name) {
        this.friendName = name;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String email) {
        this.friendEmail = email;
    }

    public LocalDate getFriendDob() {
        return friendDob;
    }

    public void setFriendDob(LocalDate friendDob) {
        this.friendDob = friendDob;
    }

    public String getFriendImgUrl() {
        return friendImgUrl;
    }

    public void setFriendImgUrl(String imageUrl) {
        this.friendImgUrl = imageUrl;
    }

    public Collection<Sighting> getSightings() {
        return sightings;
    }

    public void setSightings(Collection<Sighting> sightings) {
        this.sightings = sightings;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

}


