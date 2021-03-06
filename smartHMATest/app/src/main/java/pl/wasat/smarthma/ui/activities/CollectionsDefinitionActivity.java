package pl.wasat.smarthma.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import pl.wasat.smarthma.R;
import pl.wasat.smarthma.helper.Const;
import pl.wasat.smarthma.interfaces.OnCollectionsListSelectionListener;
import pl.wasat.smarthma.kindle.AmznAreaPickerMapFragment.OnAmznAreaPickerMapFragmentListener;
import pl.wasat.smarthma.kindle.AmznBaseMapFragment;
import pl.wasat.smarthma.model.FedeoRequestParams;
import pl.wasat.smarthma.model.iso.EntryISO;
import pl.wasat.smarthma.ui.activities.base.BaseCollectionsActivity;
import pl.wasat.smarthma.ui.frags.browse.BrowseCollectionFirstDetailFragment;
import pl.wasat.smarthma.ui.frags.browse.CollectionEmptyDetailsFragment;
import pl.wasat.smarthma.ui.frags.browse.CollectionEmptyDetailsFragment.OnCollectionEmptyDetailsFragmentListener;
import pl.wasat.smarthma.ui.frags.browse.CollectionsGroupListFragment;
import pl.wasat.smarthma.ui.frags.browse.CollectionsListFragment.OnCollectionsListFragmentListener;
import pl.wasat.smarthma.ui.frags.common.AreaPickerMapFragment;
import pl.wasat.smarthma.ui.frags.common.AreaPickerMapFragment.OnAreaPickerMapFragmentListener;
import pl.wasat.smarthma.ui.frags.common.CollectionDetailsFragment.OnCollectionDetailsFragmentListener;
import pl.wasat.smarthma.ui.frags.common.DatePickerFragment.OnDatePickerFragmentListener;
import pl.wasat.smarthma.ui.frags.common.TimePickerFragment.OnTimePickerFragmentListener;
import pl.wasat.smarthma.utils.obj.LatLngBoundsExt;
import roboguice.util.temp.Ln;

public class CollectionsDefinitionActivity extends BaseCollectionsActivity
        implements OnCollectionsListSelectionListener,
        OnCollectionsListFragmentListener,
        OnCollectionDetailsFragmentListener,
        OnCollectionEmptyDetailsFragmentListener,
        OnAreaPickerMapFragmentListener,
        OnAmznAreaPickerMapFragmentListener,
        OnDatePickerFragmentListener, OnTimePickerFragmentListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static boolean TWO_PANEL_MODE;
    private boolean detail;
    private BrowseCollectionFirstDetailFragment browseCollectionFirstDetailFragment;
    private CollectionsGroupListFragment collectionsGroupListFragment;
    protected boolean start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Ln.getConfig().setLoggingLevel(Log.ERROR);
        super.onCreate(savedInstanceState);
        start = false;
        TextView text = (TextView) findViewById(R.id.action_bar_title);
        text.setText("Searched Collections");
        detail = false;
            TWO_PANEL_MODE = true;
            loadLeftListPanel();
            loadMapWithBasicSettingsView();

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btnBackTwoPanel);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(CollectionsDefinitionActivity.this, StartActivity.class);
                startActivity(startIntent);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_eo_map_twopane, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                moveTaskToBack(true);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */


    @Override
    public void onBackPressed() {

        if (dismissMenuOnBackPressed()) return;


        FragmentManager fm = getSupportFragmentManager();
        int bsec = fm.getBackStackEntryCount();
        if (isBackStackEmpty(bsec)) return;

        String bstEntryName = fm.getBackStackEntryAt(bsec - 1).getName();
        if (detail) {

            fm.popBackStackImmediate();
            detail = false;

        } else {
            if (bstEntryName.equalsIgnoreCase("CollectionEmptyDetailsFragment")
                    || bstEntryName.equalsIgnoreCase("CollectionDetailsFragment")) {
                while (bsec > 0) {
                    fm.popBackStackImmediate();
                    bsec = fm.getBackStackEntryCount();
/*                bstEntryName = fm.getBackStackEntryAt(bsec - 1).getName();
                if (bstEntryName.equalsIgnoreCase("CollectionEmptyDetailsFragment")
                        || bstEntryName.equalsIgnoreCase("CollectionDetailsFragment"))
                    bsec = 1;*/
                }
            } else if (bstEntryName.equalsIgnoreCase("AreaPickerMapFragment")) {
                while (bstEntryName.equalsIgnoreCase("AreaPickerMapFragment")) {
                    fm.popBackStackImmediate();
                    bsec = fm.getBackStackEntryCount();
                    if (isBackStackEmpty(bsec)) return;
                    bstEntryName = fm.getBackStackEntryAt(bsec - 1).getName();
                }
            } else if (bstEntryName.equalsIgnoreCase("CollectionsListFragment")) {
                fm.popBackStackImmediate();
            } else {
                finish();
                super.onBackPressed();
            }
        }
    }


    private boolean isBackStackEmpty(int bsec) {
        if (bsec == 0) {
            finish();
            super.onBackPressed();
            return true;
        }
        return false;
    }


    /**
     *
     */
    private void loadLeftListPanel() {
        collectionsGroupListFragment = new CollectionsGroupListFragment(detail);
        Bundle args = new Bundle();
        collectionsGroupListFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_base_list_container,
                        collectionsGroupListFragment).commit();
    }

    private void loadMapWithBasicSettingsView() {
        browseCollectionFirstDetailFragment = BrowseCollectionFirstDetailFragment
                .newInstance();

        if (one_Panel)
        {
            if (start) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_base_list_container,
                                browseCollectionFirstDetailFragment,
                                "BrowseCollectionFirstDetailFragment").commit();
                detail = true;
            }
        }
        else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_base_details_container,
                                browseCollectionFirstDetailFragment,
                                "BrowseCollectionFirstDetailFragment").commit();
            }
        }



    /**
     * for fragment to find out if activity is in two-pane mode
     */
    @Override
    public boolean isTwoPaneMode() {
        return TWO_PANEL_MODE;
    }

    @Override
    public void onCollectionSelected(Integer chosenCollectionId) {

        if (chosenCollectionId == -1 ) {
            Toast.makeText(CollectionsDefinitionActivity.this,
                    R.string.specific_collection_does_not_exist,
                    Toast.LENGTH_LONG).show();
            return;
        }

            if (Const.IS_KINDLE) {
                AmznBaseMapFragment amznBaseMapFrag = new AmznBaseMapFragment();
                Bundle args = new Bundle();
                amznBaseMapFrag.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                if (one_Panel) {
                    transaction.replace(R.id.activity_base_list_container, amznBaseMapFrag);
                } else {
                    transaction.replace(R.id.activity_base_details_container, amznBaseMapFrag);
                }
                transaction.commit();
            } else {
                AreaPickerMapFragment areaPickerMapFragment = new AreaPickerMapFragment();
                Bundle args = new Bundle();
                areaPickerMapFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                if (one_Panel) {
                    transaction.replace(R.id.activity_base_list_container, areaPickerMapFragment);
                    transaction
                            .addToBackStack("AreaPickerMapFragment")
                            .commit();
                    detail = true;
                } else {
                    transaction.replace(R.id.activity_base_details_container, areaPickerMapFragment);
                    transaction
                            .addToBackStack("AreaPickerMapFragment")
                            .commit();
                }
            }
    }

    private void callUpdateFirstDetailFrag(LatLngBoundsExt bounds) {
        BrowseCollectionFirstDetailFragment browseCollectionFirstDetailFragment = (BrowseCollectionFirstDetailFragment) getSupportFragmentManager()
                .findFragmentByTag("BrowseCollectionFirstDetailFragment");
        if (browseCollectionFirstDetailFragment != null) {
            browseCollectionFirstDetailFragment.updateAreaBounds(bounds);
        }

        //TODO - Make it more universal and differentiate from which fragment it is called
        CollectionEmptyDetailsFragment collectionEmptyDetailsFragment = (CollectionEmptyDetailsFragment) getSupportFragmentManager()
                .findFragmentByTag("CollectionEmptyDetailsFragment");
        if (collectionEmptyDetailsFragment != null) {
            collectionEmptyDetailsFragment.updateAreaBounds(bounds);
        }
    }

    @Override
    public void onMapFragmentBoundsChange(LatLngBoundsExt bounds) {
        callUpdateFirstDetailFrag(bounds);
    }

    @Override
    public void onAmznMapFragmentBoundsChange(LatLngBoundsExt bounds) {
        callUpdateFirstDetailFrag(bounds);
    }

    @Override
    public void onCollectionDetailsFragmentShowProducts(FedeoRequestParams fedeoRequestParams) {
        startSearchingProductsProcess(fedeoRequestParams);
    }

    @Override
    public void onCollectionEmptyDetailsFragmentShowProducts(FedeoRequestParams fedeoRequestParams) {
        startSearchingProductsProcess(fedeoRequestParams);
    }

    @Override
    public void onCollectionEmptyDetailsFragmentShowCollections(FedeoRequestParams fedeoRequestParams) {
        startSearchingCollectionsProcess(fedeoRequestParams);
    }

    @Override
    public void onCollectionDetailsFragmentShowMetadata(EntryISO displayedEntry) {
        loadIsoMetadataFragment(displayedEntry);
    }

    @Override
    public void onDatePickerFragmentDateChoose(Calendar calendar, String viewTag) {
        browseCollectionFirstDetailFragment.setDateValues(calendar, viewTag);
    }

    @Override
    public void onTimePickerFragmentTimeChoose(Calendar calendar, String viewTag) {
        browseCollectionFirstDetailFragment.setTimeValues(calendar, viewTag);
    }

    public CollectionsGroupListFragment getCollectionsGroupListFragment() {
        return collectionsGroupListFragment;
    }
}
