package com.lihkin16.notes_apk.ui.fragments;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.lihkin16.notes_apk.R;
import com.lihkin16.notes_apk.utils.AppPrefrences;

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 9001;


    private GoogleSignInClient googleSignInClient;
    ProgressBar progressBarLogin ;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    handleSignInResult(result.getData());
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        progressBarLogin = view.findViewById(R.id.progressBarLogin);
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        // Set click listener for the Google Sign-In button
        SignInButton btnGoogleSignIn = view.findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(v ->

                signIn());

        return view;
    }

    private void signIn() {
        progressBarLogin.setVisibility(View.VISIBLE);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }




    private void handleSignInResult(Intent data) {
        if (data == null) {
            Snackbar.make(this.getView(), "Google Sign-In failed", Snackbar.LENGTH_SHORT).show();
            Log.e("SignInResult", "Intent data is null");
            return;
        }
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {

            GoogleSignInAccount account = task.getResult(ApiException.class);
            new AppPrefrences(requireActivity()).saveLoginStatus(true);
            progressBarLogin.setVisibility(View.GONE);
            Snackbar.make(this.getView(), "You have successfully login with Google", Snackbar.LENGTH_SHORT).show();
            navigateToNotesListFragment();
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(this.getView(), "Google Sign-In failed", Snackbar.LENGTH_SHORT).show();
            Log.e("SignInResult", "Exception: " + e.getMessage());
            new AppPrefrences(requireActivity()).saveLoginStatus(false);
            progressBarLogin.setVisibility(View.GONE);
        }
    }

    private void navigateToNotesListFragment() {
        // Assuming you have a NavController, replace 'your_nav_host_fragment' with the actual ID of your NavHostFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new NoteListFragment()); // Replace 'NextFragment' with the actual fragment you want to navigate to
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }


    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }
}

