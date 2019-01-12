package ro.sapientia.ms.sapvertiser.Main.Helpers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class AdvertisementReportDeleteDialog extends DialogFragment  {

    private static final String TAG = "AdReportDeleteDialog";
    private TextView reportAdvertisement;
    private TextView deleteAdvertisement;
    private TextView editAdvertisement;
    private FirebaseAuth auth;
    private FirebaseUser loggedUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private long currentAdId;
    private OnDialogButtonClicked mListener;
    private RetrieveDataListener<String > onDeleteListener;
    /*****************************************************************************************************
     The default constructor of the AdvertisementReportDeleteDialog class
     *****************************************************************************************************/
    public AdvertisementReportDeleteDialog(){

    }


    /*****************************************************************************************************
     The constructor of the AdvertisementReportDeleteDialog class
     - gets the advertisement Id as parameter
     - initiates the member variables
     *****************************************************************************************************/
    @SuppressLint("ValidFragment")
    public AdvertisementReportDeleteDialog(long adId, OnDialogButtonClicked listener, RetrieveDataListener<String> onDeleteListener){
        mListener = listener;
        currentAdId = adId;
        auth = FirebaseAuth.getInstance();
        loggedUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("ads/" + adId);
        this.onDeleteListener = onDeleteListener;
    }


    /*****************************************************************************************************
     The onCreateView method of the AdvertisementReportDeleteDialog class
     - Sets the listeners to the views.
     *****************************************************************************************************/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_advertisement_report_delete, container,false);

        reportAdvertisement = view.findViewById(R.id.dialog_report_ad);
        deleteAdvertisement = view.findViewById(R.id.dialog_delete_ad);
        editAdvertisement = view.findViewById(R.id.dialog_edit_ad);


        databaseReference.child("publishingUserId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String publishingUserId = (String)dataSnapshot.getValue();

                if(loggedUser.getPhoneNumber().equals(publishingUserId)){
                    editAdvertisement.setVisibility(View.VISIBLE);
                    editAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"Edit advertisement", Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                            Bundle bundle = new Bundle();
                            Log.d("AdRepDeleteDialog", "Advertisement id: " + currentAdId);
                            bundle.putLong("adId", currentAdId);
                            Navigation.getNavigationInstance().changeFragment(getActivity().getSupportFragmentManager(), new AdvertisementCreateFragment(),false, bundle);
                        }
                    });

                    deleteAdvertisement.setVisibility(View.VISIBLE);
                    deleteAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogInterface.OnClickListener deleteDialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            DataHandler.getDataHandlerInstance().deleteAdvertisement(currentAdId, onDeleteListener);
                                            Navigation.getNavigationInstance().changeFragment(getActivity().getSupportFragmentManager(), new AdvertisementListFragment(), false, null);
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
                    });

                }
                else {
                    reportAdvertisement.setVisibility(View.VISIBLE);
                    reportAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogInterface.OnClickListener reportDialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            databaseReference.child("isReported").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mListener.reportAdvertisementResult();

                                                }
                                            });
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
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {

        try {
            mListener = (OnDialogButtonClicked) getTargetFragment();
        }
        catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
        super.onAttach(context);
    }

}
