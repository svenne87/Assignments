package se.svenne.assignment2a;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BmiActivity extends Fragment implements View.OnClickListener {

    private Spinner spinnerLength1, spinnerLength2, spinnerLength3, spinnerWeight1, spinnerWeight2, spinnerWeight3;
    private Button btnCalculate, btnClear;
    private TextView textView_values, textView_result;
    private String lengthString1;
    private String lengthString2;
    private String lengthString3;
    private String weightString1;
    private String weightString2;
    private String weightString3;
    private String valuesString;
    private String lengthValues;
    private String weightValues;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_bmi, container, false);

        //set values
        spinnerLength1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinnerLength2 = (Spinner) rootView.findViewById(R.id.spinner2);
        spinnerLength3 = (Spinner) rootView.findViewById(R.id.spinner3);

        spinnerWeight1 = (Spinner) rootView.findViewById(R.id.spinner4);
        spinnerWeight2 = (Spinner) rootView.findViewById(R.id.spinner5);
        spinnerWeight3 = (Spinner) rootView.findViewById(R.id.spinner6);

        textView_values = (TextView) rootView.findViewById(R.id.values);
        textView_result = (TextView) rootView.findViewById(R.id.result);

        btnCalculate = (Button) rootView.findViewById(R.id.calculate_button);
        btnClear = (Button) rootView.findViewById(R.id.reset_button);

        //get the Context of the current Activity, context is passed as argument later
        //The activity is a context (since Activity extends Context).
        context = getActivity();

        //add items to spinners
        addItemsToSpinners();

        //add Itemlisteners to all spinners
        setSpinnerItemListeners(spinnerLength1);
        setSpinnerItemListeners(spinnerLength2);
        setSpinnerItemListeners(spinnerLength3);
        setSpinnerItemListeners(spinnerWeight1);
        setSpinnerItemListeners(spinnerWeight2);
        setSpinnerItemListeners(spinnerWeight3);

        //set listener
        btnCalculate.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        return rootView;
    }

    // add items into spinner dynamically
    public void addItemsToSpinners() {

        List<String> list = new ArrayList<String>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");

        //You can share the adapter between different Spinners if they adapt the same information.
        //context is passed as argument here
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLength1.setAdapter(dataAdapter);
        spinnerLength2.setAdapter(dataAdapter);
        spinnerLength3.setAdapter(dataAdapter);
        spinnerWeight1.setAdapter(dataAdapter);
        spinnerWeight2.setAdapter(dataAdapter);
        spinnerWeight3.setAdapter(dataAdapter);
    }


    public void setSpinnerItemListeners(final Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //check length spinners and set the correct string
                if(spinner == spinnerLength1){
                    lengthString1 = String.valueOf(spinner.getSelectedItem());
                } else if(spinner == spinnerLength2){
                    lengthString2 = String.valueOf(spinner.getSelectedItem());
                } else if(spinner == spinnerLength3){
                    lengthString3 = String.valueOf(spinner.getSelectedItem());
                }

                //add length values to string
                lengthValues = lengthString1 + lengthString2 + lengthString3;
                lengthValues = lengthValues.replaceAll("^0*", ""); //removes leading zeros
                valuesString = "Length: " + lengthValues + " cm";

                //check weight spinners and set correct values
                if(spinner == spinnerWeight1){
                    weightString1 = String.valueOf(spinner.getSelectedItem());
                } else if (spinner == spinnerWeight2){
                    weightString2 = String.valueOf(spinner.getSelectedItem());
                } else if (spinner == spinnerWeight3){
                    weightString3 = String.valueOf(spinner.getSelectedItem());
                }

                //add weight values to string
                weightValues = weightString1 + weightString2 + weightString3;
                weightValues = weightValues.replaceAll("^0*", ""); //removes leading zeros
                valuesString += "  Weight: " + weightValues + " kg";

                //write out all values
                textView_values.setText(valuesString);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    @Override
    public void onClick(View v){

        //if we press the calculate button
        if(v == btnCalculate){
            String note;

            if(calculateBmi() != 0){
                //write out a note about the result
                if(calculateBmi() >= 30){
                    note = "  Extremely Overweight!";
                } else if(calculateBmi() >= 25){
                    note = "  Overweight!";
                } else if(calculateBmi() >= 18.5 && calculateBmi() < 25){
                    note = "  Ideal weight";
                } else {
                    note = "  Underweight!";
                }

                String result = String.valueOf(calculateBmi());
                textView_result.setText("BMI: " + result + note);
            } else {
                Toast.makeText(context, "You have to set both weight and height!", Toast.LENGTH_LONG).show();
            }
        } else if (v == btnClear){
            resetValues();  //Clear all
        }

    }

    //calculates the bmi value
    private double calculateBmi(){

        double length = 0;
        double weight = 0;

        //if string not ""
        if(lengthValues != ""){
            length = Double.parseDouble(lengthValues);
        }

        if(weightValues != ""){
            weight = Double.parseDouble(weightValues);
        }


        if(length != 0 && weight != 0){
            double heightInMeters = length / 100;
            double bmi = weight/(heightInMeters*heightInMeters);

            //value with 2 decimals
            bmi = (double)Math.round(bmi * 100) / 100;
            return bmi;
        } else {
            return 0;
        }
    }


    //resets all values
    private void resetValues(){

        spinnerLength1.setSelection(0);
        spinnerLength2.setSelection(0);
        spinnerLength3.setSelection(0);

        spinnerWeight1.setSelection(0);
        spinnerWeight2.setSelection(0);
        spinnerWeight3.setSelection(0);

        textView_result.setText("");
        textView_values.setText("Length: cm   Weight: kg");
    }
    
}
