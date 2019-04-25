package edu.quinnipiac.ser210.starbuzz4;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {


    public static final String EXTRA_DRINKID="drinkId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        int drinkId=(Integer)getIntent().getExtras().get(EXTRA_DRINKID);
        TextView name=(TextView)findViewById(R.id.name);
        TextView description=(TextView)findViewById(R.id.description);
        ImageView photo=(ImageView)findViewById(R.id.photo);
        SQLiteOpenHelper starbuzzDatabaseHelper=new StarbuzzDatabaseHelper(this);
        try
        {
            SQLiteDatabase db=starbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor=db.query("DRINK",new String[]{"NAME","DESCRIPTION","IMAGE_RESOURCE_ID","FAVORITE"},"_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null,null,null);
            if(cursor.moveToFirst())
            {
                String nameText=cursor.getString(0);
                String descriptionText=cursor.getString(1);
                int photoId=cursor.getInt(2);
                boolean isFavorite=(cursor.getInt(3)==1);
                name.setText(nameText);
                description.setText(descriptionText);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
                CheckBox favorite=(CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();
        }
        catch (SQLException e)
        {
            Toast toast=Toast.makeText(this,"Database unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }



    }

    public void onFavoriteClicked(View view)
    {
        int drinkId=(Integer) getIntent().getExtras().get(EXTRA_DRINKID);
        new UpdateDrinkTask().execute(drinkId);
    }

    private class UpdateDrinkTask extends AsyncTask<Integer,Void,Boolean>
    {
        private ContentValues drinkValues;
        @Override
        protected void onPreExecute() {
            CheckBox favorite=(CheckBox)findViewById(R.id.favorite);
            drinkValues=new ContentValues();
            drinkValues.put("FAVORITE",favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... drinks) {
            int drinkId=drinks[0];
            SQLiteOpenHelper starbuzzDatabaseHelper=new StarbuzzDatabaseHelper(DrinkActivity.this);
            try
            {
                SQLiteDatabase db=starbuzzDatabaseHelper.getReadableDatabase();
                db.update("DRINK",drinkValues,"_id = ?",
                        new String[]{Integer.toString(drinkId)});
                db.close();
                return true;
            }
            catch (SQLException e)
            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(!success)
            {
                Toast toast=Toast.makeText(DrinkActivity.this,"Database unavailable",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
