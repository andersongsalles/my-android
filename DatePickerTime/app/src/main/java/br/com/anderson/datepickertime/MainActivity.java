package br.com.anderson.datepickertime;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static br.com.anderson.datepickertime.R.id.edt_data_inicial;

public class MainActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private int year, month, day, hour, minute;
    private EditText edtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtData = (EditText) findViewById(edt_data_inicial);

        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleTestDrive(v);
            }
        });
    }

    public void scheduleTestDrive(View view){
        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );


        Calendar cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();
        //calendario apenas superior a data atual
        cMax.set( cMax.get(Calendar.YEAR), cDefault.get(Calendar.MONTH), cDefault.get(Calendar.DAY_OF_MONTH) );
     //   datePickerDialog.setMinDate(cMin);
        datePickerDialog.setMaxDate(cMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();

        while( cAux.getTimeInMillis() <= cMax.getTimeInMillis() ){
            if( cAux.get( Calendar.DAY_OF_WEEK ) != 1 && cAux.get( Calendar.DAY_OF_WEEK ) != 7 ){
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis( cAux.getTimeInMillis() );

                daysList.add( c );
            }
            cAux.setTimeInMillis( cAux.getTimeInMillis() + ( 24 * 60 * 60 * 1000 ) );
        }
        daysArray = new Calendar[ daysList.size() ];
        for( int i = 0; i < daysArray.length; i++ ){
            daysArray[i] = daysList.get(i);
        }

       // datePickerDialog.setSelectableDays( daysArray );
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show( getFragmentManager(), "DatePickerDialog" );
    }

    private void initDateTimeData(){
        if( year == 0 ){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int ano, int mes, int dia) {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day, hour, minute);

        year = ano;
        month = mes;
        day = dia;

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                tDefault.get(Calendar.HOUR_OF_DAY),
                tDefault.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setOnCancelListener(this);
        timePickerDialog.show(getFragmentManager(), "timePickerDialog");
        timePickerDialog.setTitle("Horário Test Drive");

        timePickerDialog.setThemeDark(true);
    }


    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hora, int minuto, int s) {
        if( hora < 9 || hora > 18 ){
            onDateSet(null, year, month, day);
            Toast.makeText(this, "Somente entre 9h e 18h", Toast.LENGTH_SHORT).show();
            return;
        }

        hour = hora;
        minute = minuto;

        edtData.setText( (day < 10 ? "0"+day : day)+"/"+
                (month+1 < 10 ? "0"+(month+1) : month+1)+"/"+
                year+" às "+
                (hour < 10 ? "0"+hour : hour)+"h"+
                (minute < 10 ? "0"+minute : minute));
    }
}
