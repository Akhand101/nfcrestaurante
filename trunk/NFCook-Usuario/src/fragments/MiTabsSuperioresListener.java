package fragments;

import com.example.nfcook.R;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MiTabsSuperioresListener  implements TabListener{
	private Fragment fragment;
	private String nombreTab;
	private Activity activity;
 
	public MiTabsSuperioresListener(Fragment fragment, String nombreTab, Activity activity) {
		this.fragment = fragment;
		this.nombreTab = nombreTab;
		this.activity = activity;
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.FrameLayoutPestanas, fragment, nombreTab);
	}
 
	public void onTabSelected(Tab tab, FragmentTransaction ft) {       
		ft.replace(R.id.FrameLayoutPestanas, fragment, nombreTab);
		// Ponemos el t�tulo a la actividad
        // Recogemos ActionBar
        ActionBar actionbar = activity.getActionBar();
    	actionbar.setTitle(" CONFIGURE SU MEN�...");
	}
 
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}
}