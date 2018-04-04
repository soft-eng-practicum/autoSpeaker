package gluka.autospeakerphone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class FavoriteList extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == PICK_CONTACT)
        {
            Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);
            cursor.moveToNext();
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String  name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

            Toast.makeText(this, "Contect LIST  =  "+name, Toast.LENGTH_LONG).show();
        }
    }



}
