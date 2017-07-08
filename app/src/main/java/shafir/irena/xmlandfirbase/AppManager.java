package shafir.irena.xmlandfirbase;

import android.app.Application;
import android.icu.text.DisplayContext;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by irena on 09/06/2017.
 */
// registered in the manifest tag in the name attribute
public class AppManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // font awesome fa_android
        //http://fontawesome.io/cheatsheet/
        TypefaceProvider.registerDefaultIconSets();

    }


}
