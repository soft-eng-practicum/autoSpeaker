package gluka.autospeakerphone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackSystem extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    DatabaseReference reference;

    EditText feedback;
    EditText userName;
    Button submit;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //changes the theme before it gets displayed
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppCompatBlind);

        }
        setContentView(R.layout.activity_feedback_system);

        //Initialize Firebase instances
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // Initialize interface components
        feedback = (EditText) findViewById(R.id.feedbackEditText);
        userName = (EditText) findViewById(R.id.userNameEditText);
        submit = (Button) findViewById(R.id.submitFeedbackBtn);

        // Attach onClick listener to submit button
        submit.setOnClickListener(this);

    }

    public void sendToDatabase() {

        // Creating user email as the "child" component in FireBase
        String name = userName.getText().toString().trim();
        reference = database.getReference(name);

        // Setting the "value" in FireBase to the feedback response from user
        String feedbackResponse = feedback.getText().toString().trim();
        reference.setValue(feedbackResponse);
    }

    // Execute database commands when button clicked
    @Override
    public void onClick(View view) {
        if(view == submit) {
            sendToDatabase();

            // Reset feedback text box to empty strings when user hit submit
            feedback.setText("");
            userName.setText("");
        }
    }
}