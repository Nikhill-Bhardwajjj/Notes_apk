package com.lihkin16.notes_apk.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.lihkin16.notes_apk.R;
import com.lihkin16.notes_apk.data.model.Note;
import com.lihkin16.notes_apk.data.model.NoteSavedListener;
import com.lihkin16.notes_apk.ui.adapters.NoteAdapter;
import com.lihkin16.notes_apk.ui.fragments.AddEditNoteFragment;
import com.lihkin16.notes_apk.ui.fragments.LoginFragment;
import com.lihkin16.notes_apk.ui.fragments.NoteListFragment;
import com.lihkin16.notes_apk.utils.AppPrefrences;
import com.lihkin16.notes_apk.utils.NoteDataSource;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddEditNoteFragment.OnNoteSavedListener {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);


        if (isLoggedIn()) {
            // Load  note list fragment
            loadFragment(new NoteListFragment());
        } else {

            Toast.makeText(this, "Please login to move further", Toast.LENGTH_SHORT).show();
            loadFragment(new LoginFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_notes) {
                    if (isLoggedIn()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadFragment(new NoteListFragment());
                            }
                        }, 200);
                    } else {

                        Toast.makeText(MainActivity.this, "Please login to move further", Toast.LENGTH_SHORT).show();
                        loadFragment(new LoginFragment());
                    }
                    return true;
                } else if (item.getItemId() == R.id.menu_logout) {

                    signOutFromGoogle();
                    saveLoginStatus(false);

                    return true;
                } else {
                    return false;
                }
            }

            private void signOutFromGoogle() {
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
                googleSignInClient.signOut().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sign-out Successfully", Toast.LENGTH_SHORT).show();
                        loadFragment(new LoginFragment());
                    } else {

                        Toast.makeText(MainActivity.this, "Sign-out failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

        private boolean isLoggedIn() {
        return new AppPrefrences(this).isLoggedIn();
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        new AppPrefrences(this).saveLoginStatus(isLoggedIn);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onNoteSaved(NoteDataSource noteDataSource, NoteAdapter noteAdapter) {



        List<Note> updatedNotes = noteDataSource.getAllNotes();
        noteAdapter.setNotes(updatedNotes);
        noteAdapter.notifyDataSetChanged();

        getSupportFragmentManager().popBackStack();
        Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
    }
}

