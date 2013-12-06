package se.svenne.assignment2a;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CountriesList extends Fragment {
    private ArrayAdapter<Country> adapterCountry;
    private List<Country> countryList;
    private ListView list;
    private DatabaseHandler db;
    private Country countryToEdit;
    private Country emptyCountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_countries,
                container, false);
        list = (ListView) rootView.findViewById(R.id.listView);
        setHasOptionsMenu(true);

        countryList = new ArrayList<Country>();
        adapterCountry = new ArrayAdapter<Country>(this.getActivity(), android.R.layout.simple_list_item_1, countryList);
        list.setAdapter(adapterCountry);
        registerForContextMenu(list);

        db = new DatabaseHandler(this.getActivity());

        //To be displayed if no countries
        emptyCountry = new Country();
        emptyCountry.setCountry("No Items");

        countryList.add(emptyCountry);
        adapterCountry.add(emptyCountry);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //get all countries from db
        updateList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_country_add_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.add_country:
                Intent intent = new Intent(this.getActivity(), CountriesAdd.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        //get the country object
        countryToEdit = adapterCountry.getItem(aInfo.position);

        //if we do not select the empty item
        if(countryToEdit.getCountry() != "No Items"){
            menu.setHeaderTitle(countryToEdit.getCountry());
            menu.add(1, 1, 1, "Change");
            menu.add(1, 2, 2, "Delete");
        }

    }


    // This method is called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == 1){
            //change the country

            // Set view to get user input
            LinearLayout lila = new LinearLayout(getActivity());
            lila.setOrientation(1); //1 is for vertical orientation
            final EditText inputCountry = new EditText(getActivity());
            final EditText inputYear = new EditText(getActivity());
            lila.addView(inputCountry);
            lila.addView(inputYear);

            //set values to edit fields
            inputCountry.setText(countryToEdit.getCountry());
            inputYear.setText(countryToEdit.getYear());

            //create alert dialog
            final AlertDialog alert = new AlertDialog.Builder(getActivity())
                    .setTitle("Edit Country")
                    .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                    .setNegativeButton(android.R.string.cancel, null)
                    .setView(lila)
                    .create();

            //using onShowListener so input can be validated without the dialog disappear
            alert.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //You will get as string input data in this variable.
                            String inputCountryToChange = inputCountry.getEditableText().toString();
                            String inputYearToChange = inputYear.getEditableText().toString();

                            //validate strings
                            boolean countryName = false;
                            boolean countryYear = false;

                            //check countryName input
                            if(inputCountryToChange .length() > 40){
                                Toast.makeText(getActivity(), "You can't write more than 40 characters!", Toast.LENGTH_LONG).show();
                            } else if(inputCountryToChange .length() < 3){
                                Toast.makeText(getActivity(), "You have to write more than 3 characters!", Toast.LENGTH_LONG).show();
                            } else if(inputCountryToChange  == ""){
                                Toast.makeText(getActivity(), "You have to write something!", Toast.LENGTH_LONG).show();
                            } else if(inputCountryToChange .trim().length() == 0 ){
                                Toast.makeText(getActivity(), "You have to write something!", Toast.LENGTH_LONG).show();
                            } else {
                                countryName = true; //set to true
                            }

                            //check year input
                            if(inputYearToChange.length() < 4){
                                Toast.makeText(getActivity(), "You have to set a year!", Toast.LENGTH_LONG).show();
                            } else {
                                countryYear = true;
                            }

                            //if all checks out
                            if(countryYear != false && countryName != false){
                                //set new values
                                countryToEdit.setCountry(inputCountryToChange);
                                countryToEdit.setYear(inputYearToChange);

                                //update database
                                db.updateCountry(countryToEdit);

                                //update list
                                int index = countryList.indexOf(countryToEdit); //get the index of the object to alter
                                countryList.set(index, countryToEdit); //set the country object on the same position in list

                                //update adapter
                                adapterCountry.notifyDataSetChanged();

                                Toast.makeText(getActivity(), "Country updated!", Toast.LENGTH_LONG).show();
                                alert.dismiss(); //everything was ok, dismiss dialog
                            }
                        }
                    });
                }
            });

            alert.show();

        } else if(itemId == 2){
            //remove country from: database, list and adapter
            db.deleteCountry(countryToEdit);
            countryList.remove(countryToEdit);
            adapterCountry.remove(countryToEdit);

            //if we have remove all countries
            if(countryList.isEmpty()){
                //add emptyCountry to both list and adapter
                countryList.add(emptyCountry);
                adapterCountry.add(emptyCountry);
            }

            Toast.makeText(getActivity(), "Country removed!", Toast.LENGTH_LONG).show();

        }

        return true;

    }


    private void updateList(){

        //first empty the list (to clear the "emptyCountry")
        countryList.clear();

        //fill list from db
        countryList = db.getAllContacts();

        if(!countryList.isEmpty()){
            for (Country country : countryList) {
                //if the list is not empty
                adapterCountry.add(country);
            }
        } else {
            //if the list is empty
            adapterCountry.add(emptyCountry);
        }

    }
}
