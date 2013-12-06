package se.svenne.assignment2a;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CountriesAdd extends Activity implements View.OnClickListener {
    private TextView countryText;
    private TextView yearText;
    private Button addBtn;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_add_new);

        countryText = (TextView) findViewById(R.id.countryEditText);
        yearText = (TextView) findViewById(R.id.yearEditText);
        addBtn = (Button) findViewById(R.id.addButton);
        addBtn.setOnClickListener(this);

        db = new DatabaseHandler(this);
    }

    @Override
    public void onClick(View v) {

        String countryNameString = countryText.getText().toString();
        boolean name = false;

        String countryYearString = yearText.getText().toString();
        boolean year = false;


        //check countryName input
        if(countryNameString.length() > 40){
            Toast.makeText(this, "You can't write more than 40 characters!", Toast.LENGTH_LONG).show();
        } else if(countryNameString.length() < 3){
            Toast.makeText(this, "You have to write more than 3 characters!", Toast.LENGTH_LONG).show();
        } else if(countryNameString == ""){
            Toast.makeText(this, "You have to write something!", Toast.LENGTH_LONG).show();
        } else if(countryNameString.trim().length() == 0 ){
            Toast.makeText(this, "You have to write something!", Toast.LENGTH_LONG).show();
        } else {
            name = true; //set to true
        }

        //check year input
        if(countryYearString.length() < 4){
            Toast.makeText(this, "You have to set a year!", Toast.LENGTH_LONG).show();
        } else {
            year = true;
        }


        //if both values are ok
        if(year != false && name != false){
            Country c = new Country(countryNameString, countryYearString);
            db.addCountry(c);

            finish();
        }
    }
}
