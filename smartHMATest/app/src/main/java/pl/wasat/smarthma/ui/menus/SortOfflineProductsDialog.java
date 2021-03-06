package pl.wasat.smarthma.ui.menus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.List;

import pl.wasat.smarthma.R;
import pl.wasat.smarthma.SmartHMApplication;
import pl.wasat.smarthma.helper.Const;
import pl.wasat.smarthma.helper.DataSorter;
import pl.wasat.smarthma.model.entry.Entry;
import pl.wasat.smarthma.ui.activities.FavouriteProductsActivity;
import pl.wasat.smarthma.ui.frags.common.ProductsListFragmentOffline;

/**
 * Handles user interaction related to sorting data.
 */
public class SortOfflineProductsDialog extends DialogFragment {
    private FavouriteProductsActivity activity;

    public void setActivity(FavouriteProductsActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.action_sort)
                .setItems(R.array.sorting_modes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ZX", which + "");
                        try {
                            if (which == 0) {
                                SmartHMApplication.sortingType = Const.SORT_BY_TITLE_ASCENDING;
                            } else if (which == 1) {
                                SmartHMApplication.sortingType = Const.SORT_BY_TITLE_DESCENDING;
                            } else if (which == 2) {
                                SmartHMApplication.sortingType = Const.SORT_BY_DATE_ASCENDING;
                            } else if (which == 3) {
                                SmartHMApplication.sortingType = Const.SORT_BY_DATE_DESCENDING;
                            }

                            Log.d("ZX", "Sorting entries.");
                            DataSorter sorter = new DataSorter();
                            ProductsListFragmentOffline productsListFragment = activity.getProductsListFragment();
                            List<Entry> entryList = productsListFragment.getEntryList();
                            sorter.sort(entryList);
                            productsListFragment.refreshList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            /*
            builder.setMessage(R.string.search_history_item)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
            */
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
