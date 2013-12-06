package se.svenne.assignment2a;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RandomString extends Fragment implements View.OnClickListener{

    private TextView textView;
    private Button generate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_randomstring, container, false);
        textView = (TextView) rootView.findViewById(R.id.textView);
        generate = (Button) rootView.findViewById(R.id.generate_random);

        //generate a number at start
        generateNumber();
        generate.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        generateNumber();
    }

    //generates a random number (1-00)
    public void generateNumber(){
        int number = (int)(Math.random() * 100 + 1);
        String numberString = Integer.toString(number);
        textView.setText(numberString);
    }

}
