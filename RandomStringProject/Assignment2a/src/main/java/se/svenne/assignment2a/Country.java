package se.svenne.assignment2a;

public class Country {

    //private variables
    int _id;
    String _country;
    String _year;

    // Empty constructor
    public Country() {

    }

    // constructor
    public Country(int id, String country, String year) {
        this._id = id;
        this._country = country;
        this._year = year;
    }

    // constructor
    public Country(String country, String _year) {
        this._country = country;
        this._year = _year;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting country
    public String getCountry() {
        return this._country;
    }

    // setting country
    public void setCountry(String country) {
        this._country = country;
    }

    // getting year
    public String getYear() {
        return this._year;
    }

    // setting year
    public void setYear(String year) {
        this._year = year;
    }

    @Override
    public String toString() {

        if(_year != null){
            return _country + ", " + _year;
        } else {
            return _country;
        }
    }

}
