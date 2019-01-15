package ro.sapientia.ms.sapvertiser.Main.Helpers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Main.Fragments.AdvertisementCreateFragment;
import ro.sapientia.ms.sapvertiser.Main.Fragments.AdvertisementListFragment;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.OnDialogButtonClicked;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
import ro.sapientia.ms.sapvertiser.Navigation;
import ro.sapientia.ms.sapvertiser.R;

import static android.view.View.GONE;

public class AdvertisementReportDeleteDialog extends DialogFragment {

    private static final String TAG = "AdReportDeleteDialog";
    private TextView reportAdvertisement;
    private TextView deleteAdvertisement;
    private TextView editAdvertisement;
    private ImageView reportAdvertisementIcon;
    private ImageView deleteAdvertisementIcon;
    private ImageView editAdvertisementIcon;
    private FirebaseAuth auth;
    private FirebaseUser loggedUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private long currentAdId;
    private OnDialogButtonClicked mListener;
    private RetrieveDataListener<String> onDeleteListener;
    private RetrieveDataListener<String> onReportListener;

    /*****************************************************************************************************
     The default constructor of the AdvertisementReportDeleteDialog class
     *****************************************************************************************************/
    public AdvertisementReportDeleteDialog() {

    }


    /*****************************************************************************************************
     The constructor of the AdvertisementReportDeleteDialog class
     - gets the advertisement Id as parameter
     - initiates the member variables
     *****************************************************************************************************/
    @SuppressLint("ValidFragment")
    public AdvertisementReportDeleteDialog(long adId, OnDialogButtonClicked listener, RetrieveDataListener<String> onDeleteListener, RetrieveDataListener<String> onReportListener) {
        mListener = listener;
        currentAdId = adId;
        auth = FirebaseAuth.getInstance();

        loggedUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("ads/" + adId);
        this.onDeleteListener = onDeleteListener;
        this.onReportListener = onReportListener;
    }


    /*****************************************************************************************************
     The onCreateView method of the AdvertisementReportDeleteDialog class
     - Sets the listeners to the views.
     *****************************************************************************************************/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_advertisement_report_delete, container, false);

        reportAdvertisement = view.findViewById(R.id.dialog_report_ad);
        deleteAdvertisement = view.findViewById(R.id.dialog_delete_ad);
        editAdvertisement = view.findViewById(R.id.dialog_edit_ad);
        reportAdvertisementIcon = view.findViewById(R.id.dialog_report_ad_image);
        deleteAdvertisementIcon = view.findViewById(R.id.dialog_delete_ad_image);
        editAdvertisementIcon = view.findViewById(R.id.dialog_edit_ad_image);

        databaseReference.child("publishingUserId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String publishingUserId = (String) dataSnapshot.getValue();
                String loggedUserNumber = "+16505553434";
                // if(loggedUser.getPhoneNumber().equals(publishingUserId)){
                if (loggedUserNumber.equals(publishingUserId)) {
                    reportAdvertisementIcon.setVisibility(GONE);
                    reportAdvertisement.setVisibility(GONE);
                    editAdvertisement.setVisibility(View.VISIBLE);
                    editAdvertisementIcon.setVisibility(View.VISIBLE);
                    editAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editAction();
                        }
                    });
                    editAdvertisementIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editAction();
                        }
                    });
                    deleteAdvertisement.setVisibility(View.VISIBLE);
                    deleteAdvertisementIcon.setVisibility(View.VISIBLE);
                    deleteAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteAction();
                        }
                    });

                    deleteAdvertisementIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteAction();
                        }
                    });
                } else {
                    deleteAdvertisement.setVisibility(View.GONE);
                    deleteAdvertisementIcon.setVisibility(View.GONE);
                    editAdvertisement.setVisibility(View.GONE);
                    editAdvertisementIcon.setVisibility(View.GONE);
                    reportAdvertisement.setVisibility(View.VISIBLE);
                    reportAdvertisementIcon.setVisibility(View.VISIBLE);
                    reportAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reportAction();
                        }
                    });
                    reportAdvertisementIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reportAction();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void editAction() {
        getDialog().dismiss();
        Bundle bundle = new Bundle();
        Log.d("AdRepDeleteDialog", "Advertisement id: " + currentAdId);
        bundle.putLong("adId", currentAdId);
        Navigation.getNavigationInstance().changeFragment(getActivity().getSupportFragmentManager(), new AdvertisementCreateFragment(), true, bundle, "AdCreateFragment");

    }

    private void deleteAction() {
        DialogInterface.OnClickListener deleteDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DataHandler.getDataHandlerInstance().deleteAdvertisement(currentAdId, onDeleteListener);
                        Navigation.getNavigationInstance().changeFragment(getActivity().getSupportFragmentManager(), new AdvertisementListFragment(), false, null, "AdListFragment");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
                getDialog().dismiss();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you really want to delete this advertisement?")
                .setPositiveButton("Yes", deleteDialogClickListener)
                .setNegativeButton("No", deleteDialogClickListener).show();
    }


    private void reportAction() {
        DialogInterface.OnClickListener reportDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DataHandler.getDataHandlerInstance().reportAdvertisement(currentAdId, onReportListener);
                        Navigation.getNavigationInstance().changeFragment(getActivity().getSupportFragmentManager(), new AdvertisementListFragment(), false, null, "AdListFragment");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
                getDialog().dismiss();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you really want to report this advertisement?")
                .setPositiveButton("Yes", reportDialogClickListener)
                .setNegativeButton("No", reportDialogClickListener).show();
    }

    @Override
    public void onAttach(Context context) {

        try {
            mListener = (OnDialogButtonClicked) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
        super.onAttach(context);
    }

}
