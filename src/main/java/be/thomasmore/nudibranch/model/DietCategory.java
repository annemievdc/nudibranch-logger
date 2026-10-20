package be.thomasmore.nudibranch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@Entity
public class DietCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @NotNull(message = "Categorienaam is verplicht")
    private String categoryName;

    @OneToMany(mappedBy = "dietCategory")
    private Collection<Diet> diet;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }



    public Collection<Diet> getDiet() {
        return diet;
    }

    public void setDiet(Collection<Diet> diet) {
        this.diet = diet;
    }
}
