package shafir.irena.xmlandfirbase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyFragment extends Fragment implements CurrencyDataSource.onCurrencyArrivedListener {


    public CurrencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        CurrencyDataSource.getCurrency(this);

        return view;
    }

    @Override
    public void onCurrencyArrived(final List<CurrencyDataSource.Currency> data, final Exception e) {
        // No UI Updating on a secondary thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e == null) {
                    // all is good
                    Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "pls check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}
