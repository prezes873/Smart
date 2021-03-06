package pl.wasat.smarthma.ui.menus;

import android.view.View;
import android.widget.LinearLayout;

import pl.wasat.smarthma.R;
import pl.wasat.smarthma.ui.activities.FavouriteProductsActivity;

/**
 * Used for handling menu in the FavouriteProductsActivity class.
 */
public class OfflineProductsBrowserMenuHandler extends MenuHandler {
    public OfflineProductsBrowserMenuHandler(FavouriteProductsActivity activity, int menuButtonID) {
        super(activity, menuButtonID);
    }

    /**
     * Inflates the popup window layout.
     */
    protected void inflateLayout() {
        layout = layoutInflater.inflate(R.layout.popup_menu_offline_items, null);
    }

    /**
     * Creates on click listeners for specific buttons.
     */
    protected void addListeners() {
        LinearLayout sortLayout = (LinearLayout) layout.findViewById(R.id.popup_sort_layout);
        clickableViews.add(sortLayout);
        sortLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SortOfflineProductsDialog newFragment = new SortOfflineProductsDialog();
                newFragment.setActivity((FavouriteProductsActivity) activity);
                newFragment.show(activity.getSupportFragmentManager(), "Sorting_Options");
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

        LinearLayout clearLayout = (LinearLayout) layout.findViewById(R.id.popup_removeall_layout);
        clickableViews.add(clearLayout);
        clearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClearOfflineProductsDialog newFragment = new ClearOfflineProductsDialog();
                newFragment.setActivity((FavouriteProductsActivity) activity);
                newFragment.show(activity.getSupportFragmentManager(), "Clear_list_confirmation");
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

        /*
        LinearLayout favouriteProductsLayout = (LinearLayout) layout.findViewById(R.id.popup_favourites_layout);
        clickableViews.add(favouriteProductsLayout);
        favouriteProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(activity, FavouriteProductsActivity.class);
                //intent.setClass(activity, GlobalSettingsActivity.class);
                Log.d("ZX", "Starting FavouriteProductsActivity...");
                activity.startActivity(intent);
                //activity.startActivityForResult(intent, Const.REQUEST_CODE_GLOBAL_SETTINGS);
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        */

        addCommonListeners();
    }
}
