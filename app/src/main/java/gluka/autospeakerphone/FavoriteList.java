package gluka.autospeakerphone;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FavoriteList extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;
    ArrayList<String> contacts = new ArrayList<>();
    LinearLayout linearLayout;
    LinearLayout.LayoutParams layoutParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        this.addContacts();
        linearLayout = (LinearLayout) findViewById(R.id.table);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(24, 10, 24, 10);

        this.addButtons(contacts);
    }

    public void addButtons(ArrayList<String> buttons){
        int buttonID = 1;
        for(String t : buttons){
            Button temp = new Button(this);
            temp.setBackgroundColor(Color.DKGRAY);
            temp.setTextColor(Color.WHITE);
            temp.setText(t);
            linearLayout.addView(temp,layoutParams);


        }
    }
    public void addContacts(){

        //to store name-number pair

        try {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String fav = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                if(fav.equals("1")){
                    contacts.add(name);
                }
            }
            phones.close();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("error", "you need permission");
        }
    }
}
