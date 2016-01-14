package pl.wasat.smarthma.ui.frags.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.HashMap;

import pl.wasat.smarthma.R;
import pl.wasat.smarthma.helper.Const;
import pl.wasat.smarthma.preferences.GlobalPreferences;
import pl.wasat.smarthma.preferences.SharedPrefs;
import pl.wasat.smarthma.ui.activities.SearchCollectionResultsActivity;
import pl.wasat.smarthma.ui.frags.search.SearchFragment;
import pl.wasat.smarthma.ui.tooltips.Tooltip;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link null}
 * interface to handle interaction events. Use the
 * {@link BaseSearchSideParametersFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class BaseSearchSideParametersFragment extends BaseDateTimeAreaContainerFragment {
    protected View rootView;
    private static TextView tvCatalogName;
    protected static boolean one_panel;
    private static final CharSequence[] cataloguesList = {"EOP:ESA:FEDEO",
            "EOP:ESA:FEDEO:COLLECTIONS", "EOP:ESA:GPOD-EO", "EOP:ESA:EO-VIRTUAL-ARCHIVE4",
            "EOP:ESA:SMOS", "EOP:JAXA:CATS-I", "EOP:NASA:ECHO"};

    private SearchFragment.OnSearchFragmentListener mListener;
    private Intent searchIntent;
    private Tooltip tooltip;
    private HashMap extraParams;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @return A new instance of fragment SearchBasicInfoRightFragment.
     */
    public static BaseSearchSideParametersFragment newInstance() {

        return new BaseSearchSideParametersFragment();
    }

    public BaseSearchSideParametersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (one_panel)
        {
            rootView = inflater.inflate(
                    R.layout.fragment_search_side_parameters_one_panel, container, false);
            SearchManager searchManager = (SearchManager) getActivity()
                    .getSystemService(Context.SEARCH_SERVICE);

            final SearchView searchView = (SearchView) rootView
                    .findViewById(R.id.search_frag_searchview);
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getActivity().getComponentName()));

            searchView.setSubmitButtonEnabled(true);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.clearFocus();

            GlobalPreferences globalPreferences = new GlobalPreferences(getActivity());
            if (globalPreferences.getIsParamsSaved()) {
                SharedPrefs sharedPrefs = new SharedPrefs(getActivity());
                searchView.setQuery(sharedPrefs.getQueryPrefs(), false);
            }

            searchIntent = getActivity().getIntent();

            LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
            LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
            LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
            AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3
                    .getChildAt(0);
            autoComplete.setTextSize(20);
            autoComplete.setTextColor(getResources().getColor(R.color.text_black));

            Button btnGo = (Button) rootView.findViewById(R.id.search_frag_button_start);
            btnGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startSearchWithButton(searchView);
                }
            });


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    mListener.onSearchFragmentSendExtraParams(extraParams);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

            tooltip = new Tooltip(getContext(), Tooltip.TYPE_BELOW, "Greetings from below!");

            extraParams = new HashMap();
        }
        else {
            rootView = inflater.inflate(
                    R.layout.fragment_search_side_parameters, container, false);
        }


        tvCatalogName = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_catalog_name);

        tvCatalogName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCatalogueListDialog();
            }
        });

        tvAreaSWLat = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_area_sw_lat);
        tvAreaSWLon = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_area_sw_lon);
        tvAreaNELat = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_area_ne_lat);
        tvAreaNELon = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_area_ne_lon);

        LinearLayout areaLayout = (LinearLayout) rootView
                .findViewById(R.id.search_frag_side_params_layout_area);
        areaLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDeviceAndLoadMapPicker(R.id.activity_base_details_container);
            }
        });

        tvStartDate = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_start_date);
        tvStartDate.setTag("tvStartDate");
        tvStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(calStart, tvStartDate);
            }
        });
        tvStartTime = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_start_time);
        tvStartTime.setTag("tvStartTime");
        tvStartTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog(calStart, tvStartTime);
            }
        });
        tvEndDate = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_end_date);
        tvEndDate.setTag("tvEndDate");
        tvEndDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(calEnd, tvEndDate);
            }
        });
        tvEndTime = (TextView) rootView
                .findViewById(R.id.search_frag_side_params_tv_end_time);
        tvEndTime.setTag("tvEndTime");
        tvEndTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog(calEnd, tvEndTime);
            }
        });

        obtainGlobalSettings();
        return rootView;
    }


    @Override
    public void onResume() {
        sharedPrefs.setParentIdPrefs(tvCatalogName.getText().toString());
        super.onResume();
    }

    private void showCatalogueListDialog() {
        CatalogueListDialogFragment listDialFrag = new CatalogueListDialogFragment();
        listDialFrag.show(getActivity().getSupportFragmentManager(),
                "CatalogueListDialogFragment");
    }

    private void startSearchWithButton(SearchView searchView) {
        Intent intentBtnSearch = new Intent(getActivity(),
                SearchCollectionResultsActivity.class);
        intentBtnSearch.setAction("android.intent.action.SEARCH");
        intentBtnSearch.putExtra(SearchManager.QUERY, searchView.getQuery().toString());
        intentBtnSearch.putExtra(Const.KEY_INTENT_FEDEO_REQUEST_PARAMS_EXTRA, extraParams);

        mListener.onSearchFragmentStartSearchingWithButton(intentBtnSearch);
    }

    public void setAdditionalParams(String parameterKey, String parameterValue) {
        extraParams.put(parameterKey, parameterValue);
    }

    public void setQuery(String query) {
        if (rootView == null) return;
        SearchView searchView = (SearchView) rootView.findViewById(R.id.search_frag_searchview);
        if (searchView != null) searchView.setQuery(query, false);
    }


    public static class CatalogueListDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.eo_catalogue_list_title).setItems(
                    cataloguesList, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            setCatalogue(which);
                        }
                    });
            return builder.create();
        }
    }

    private static void setCatalogue(int which) {
        tvCatalogName.setText(cataloguesList[which]);
        sharedPrefs.setParentIdPrefs(cataloguesList[which].toString());
    }

    public void setCatalogue(String catalogue) {
        CharSequence[] cataloguesList = getCataloguesList();
        for (int i = 0; i < cataloguesList.length; i++) {
            if (catalogue.equalsIgnoreCase(cataloguesList[i].toString())) {
                setCatalogue(i);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            mListener = (SearchFragment.OnSearchFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSearchFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @SuppressWarnings("SameReturnValue")
    private static CharSequence[] getCataloguesList() {
        return cataloguesList;
    }

    public interface OnSearchFragmentListener {
        void onSearchFragmentBasicParamsChoose();

        void onSearchFragmentAdvanceParamsChoose();

        void onSearchFragmentSendExtraParams(HashMap extra);

        void onSearchFragmentStartSearchingWithButton(Intent intent);

    }

}
