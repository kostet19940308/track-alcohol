package nagaiko.track_alcohol;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.sql.DataSource;

import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.fragments.CategoryListFragment;
import nagaiko.track_alcohol.fragments.CocktailListFragment;

import static nagaiko.track_alcohol.api.ApiResponseTypes.CATEGORIES_LIST;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DataStorage.Subscriber {

    public final String LOG_TAG = this.getClass().getSimpleName();

    private Fragment fragment;
    private CocktailListFragment cocktailListFragment;

    private DataStorage dataStorage;
    private List<String> categories;
    private NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final FragmentManager fm = getSupportFragmentManager();
        dataStorage = DataStorage.getInstanceOrCreate(this, new Handler(getMainLooper()));
        dataStorage.getCategories(this);
//        categories = dataStorage.getCategories().toArray(new String[dataStorage.getCategories().size()]);

        if (savedInstanceState == null) {
            fragment = new CategoryListFragment();
            fm.beginTransaction().replace(R.id.fragment, fragment, CategoryListFragment.TAG).commit();
        }else {
            fragment = getSupportFragmentManager().findFragmentByTag(CategoryListFragment.TAG);
        }

    }


    @Override
    protected void onStart(){
        super.onStart();

        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "onPause");

    }

    @Override
    protected void onStop(){
        super.onStop();

        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String category = item.getTitle().toString();
        changeFragment(category);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
    private void changeFragment(String category) {
        final FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();
        cocktailListFragment = new CocktailListFragment();
        toolbar.setTitle(category);
        cocktailListFragment.setCategory(category);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, cocktailListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDataLoaded(int type, Response response) {
        if (type == CATEGORIES_LIST) {
            categories = (List<String>)response.content;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Menu menu = navigationView.getMenu();
                    for(String cat: categories) {
                        menu.add(cat).setIcon(R.drawable.coctail);
                    }
                }
            });
//            int size = categories.length;
        }
    }

    @Override
    public void onDataLoadFailed() {

    }
}
