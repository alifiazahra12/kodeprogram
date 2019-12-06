package polpos.room.discussion.ruangdiskusi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 100; // NOMER BEBAS
    List<AuthUI.IdpConfig> providers;

    Button btn_sign_out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_sign_out.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //init provider

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(), // Email Builder
                new AuthUI.IdpConfig.PhoneBuilder().build(), //
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        showSignInOptions();
    }

    private void showSignInOptions() {
      startActivityForResult(
              AuthUI.getInstance().createSignInIntentBuilder()
              .setAvailableProviders(providers)
              .setTheme(R.style.MyTheme)
              .build(),MY_REQUEST_CODE
      );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK)
            {
                //GetUser
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //show email user
                assert user != null;
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                //get button sign out
                btn_sign_out.setEnabled(true);
            }
            else
            {
                assert response != null;
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
