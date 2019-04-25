package edu.quinnipiac.ser210.starbuzz4;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor favoritesCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupOptionsListView();
        setupFavoritesListView();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent intent=new Intent(MainActivity.this,DrinkCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };
        ListView listview=(ListView)findViewById(R.id.list_options);
        listview.setOnItemClickListener(itemClickListener);

    }

    private void setupFavoritesListView() {
        ListView listFavorites=(ListView)findViewById(R.id.list_favorites);
        try
        {
            SQLiteOpenHelper starbuzzDatabaseHelper= new StarbuzzDatabaseHelper(this);
            db=starbuzzDatabaseHelper.getReadableDatabase();
            favoritesCursor=db.query("DRINK",new String[]{"_id","NAME"},"FAVORITE = 1",null,null,null,null);
            CursorAdapter favoriteAdapter=new SimpleCursorAdapter(MainActivity.this,android.R.layout.simple_list_item_1,
                    favoritesCursor,new String[] {"NAME"},new int[]{android.R.id.text1},0);
            listFavorites.setAdapter(favoriteAdapter);
        }
        catch (SQLException e)
        {
            Toast toast=Toast.makeText(this,"Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID,(int)id);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupOptionsListView()
    {
        AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent intent=new Intent(MainActivity.this,DrinkCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };
        ListView listView=(ListView)findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }
}