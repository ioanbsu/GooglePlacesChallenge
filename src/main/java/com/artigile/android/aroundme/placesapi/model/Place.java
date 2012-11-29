package com.artigile.android.aroundme.placesapi.model;

import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 1:31 PM
 */
public class Place {
    private String id;

    private List<AddressComponent> addressComponents;

    private List<PlaceEvent> placeEvents;

    private String name;

    private String vicinity;

    private List<String> types;

    private Geometry geometry;

    private Float rating;

    private String icon;

    private String reference;

    private List<PlaceReview> placeReview;

    private String formattedAddress;

    private String formattedPhoneNumber;

    private String internationalPhoneNumber;

    private OpeningHours openingHours;

    private Integer utcOffset;

    private String website;

    private boolean hasDetailedInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public List<PlaceEvent> getPlaceEvents() {
        return placeEvents;
    }

    public void setPlaceEvents(List<PlaceEvent> placeEvents) {
        this.placeEvents = placeEvents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<PlaceReview> getPlaceReview() {
        return placeReview;
    }

    public void setPlaceReview(List<PlaceReview> placeReview) {
        this.placeReview = placeReview;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isHasDetailedInfo() {
        return hasDetailedInfo;
    }

    public void setHasDetailedInfo(boolean hasDetailedInfo) {
        this.hasDetailedInfo = hasDetailedInfo;
    }

    @Override
    public String toString() {
        return name +" -" +formattedAddress+", rating:" +rating;
    }
}
