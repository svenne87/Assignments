package se.svenne.assignment2a;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class RGB extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private TextView textViewRed, textViewGreen, textViewBlue;
    private SeekBar seekBarRed, seekBarGreen, seekBarBlue;
    private Button changeBackgroundBtn;
    private int rgbBlue, rgbGreen, rgbRed;
    View background, background2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_rgb, container, false);


        textViewRed = (TextView) rootView.findViewById(R.id.textView_red);
        textViewGreen = (TextView) rootView.findViewById(R.id.textView_green);
        textViewBlue = (TextView) rootView.findViewById(R.id.textView_blue);

        seekBarBlue = (SeekBar) rootView.findViewById(R.id.seekBar_blue);
        seekBarBlue.setOnSeekBarChangeListener(this);

        seekBarGreen = (SeekBar) rootView.findViewById(R.id.seekBar_green);
        seekBarGreen.setOnSeekBarChangeListener(this);

        seekBarRed= (SeekBar) rootView.findViewById(R.id.seekBar_red);
        seekBarRed.setOnSeekBarChangeListener(this);

        background = rootView.findViewById(R.id.background);
        background2 = rootView.findViewById(R.id.background_2);

        changeBackgroundBtn = (Button) rootView.findViewById(R.id.generate_button);
        changeBackgroundBtn.setOnClickListener(this);

        return rootView;

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //set rgb values and write out the values
        if(seekBar == seekBarRed){
            rgbRed = seekBarRed.getProgress();
            textViewRed.setText("Red: " + Integer.toString(rgbRed));
        } else if (seekBar == seekBarGreen){
            rgbGreen = seekBarGreen.getProgress();
            textViewGreen.setText("Green: " + Integer.toString(rgbGreen));
        } else if (seekBar == seekBarBlue){
            rgbBlue = seekBarBlue.getProgress();
            textViewBlue.setText("Blue: " + Integer.toString(rgbBlue));
        }

        onClick(changeBackgroundBtn);  // will be called when we change color
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if(v == changeBackgroundBtn){
            //set background
            background.setBackgroundColor(Color.rgb(rgbRed, rgbGreen, rgbBlue));
            background2.setBackgroundColor(Color.rgb(rgbRed, rgbGreen, rgbBlue));

            //change color on textview to the opposite
            textViewRed.setTextColor(Color.rgb(255 - rgbRed, 255 - rgbGreen, 255 - rgbBlue));
            textViewBlue.setTextColor(Color.rgb(255 - rgbRed, 255 - rgbGreen, 255 - rgbBlue));
            textViewGreen.setTextColor(Color.rgb(255 - rgbRed, 255 - rgbGreen, 255 - rgbBlue));
        }
    }
}

