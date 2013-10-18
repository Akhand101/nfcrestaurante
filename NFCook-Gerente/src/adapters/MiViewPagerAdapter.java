package adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Alejandro Moran
 * 
 * PagerAdapter es una clase que gestiona los Fragments para
 * que se mantengan en memoria como cuando se usa en pesta�as como en
 * nuestro caso.
 * 
 * Tiene una lista de los Fragments que estamos usando.
 * 
 * En las pesta�as de la interfaz de camarero se guarda por orden:
 * 0-> Informaci�n
 * 1-> Empleados
 * 2-> Estad�sticas
 * 3-> Clasificaci�n de platos
 * 
 * Cuando el TabHost o el ViewPager necesite acceder a un Fragment
 * invocar� a getItem.
 *
 */
public class MiViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    
    public MiViewPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
    }

	@Override
    public Fragment getItem(int position) {
            return this.fragments.get(position);
    }
    
    @Override
    public int getCount() {
            return this.fragments.size();
    }
}