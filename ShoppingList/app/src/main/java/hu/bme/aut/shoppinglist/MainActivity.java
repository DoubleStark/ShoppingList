package hu.bme.aut.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hu.bme.aut.shoppinglist.adapter.ShoppingListRecycleAdapter;
import hu.bme.aut.shoppinglist.touch.ShoppingItemTouchHelperCallback;


public class MainActivity extends AppCompatActivity
{
    public static final String KEY_SHOPPINGITEM_ID = "KEY_SHOPPINGITEM_ID";
    public static final int REQUEST_CODE_EDIT = 101;

    private ShoppingListRecycleAdapter shoppinglistRecycleAdapter;
    private RecyclerView recyclerShoppinglist;

    private int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        setupUI();
    }

    private void setupUI()
    {
        setUpToolBar();
        setUpAddShoppingItemUI();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerShoppinglist = (RecyclerView) findViewById(R.id.recyclerShoppinglist);
        recyclerShoppinglist.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerShoppinglist.setLayoutManager(layoutManager);

        shoppinglistRecycleAdapter = new ShoppingListRecycleAdapter(this, ((MainApplication)getApplication()).getRealmItem());
        recyclerShoppinglist.setAdapter(shoppinglistRecycleAdapter);

        // adding touch support
        ItemTouchHelper.Callback callback = new ShoppingItemTouchHelperCallback(shoppinglistRecycleAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerShoppinglist );
    }

    private void setUpAddShoppingItemUI() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                showAddShoppingItemDialog();
            }
        });
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void showAddShoppingItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Shopping Item");

        final EditText etShoppingItemText = new EditText(this);
        builder.setView(etShoppingItemText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //shoppinglistRecycleAdapter.addShoppingItem(new ShoppingItem(etTodoText.getText().toString(), false));
                shoppinglistRecycleAdapter.addShoppingItem(etShoppingItemText.getText().toString());
                recyclerShoppinglist.scrollToPosition(0);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void openEditActivity(int index, String itemID)
    {
        positionToEdit = index;

        Intent startEdit = new Intent(this, EditItemActivity.class);

        startEdit.putExtra(KEY_SHOPPINGITEM_ID, itemID);

        startActivityForResult(startEdit, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE_EDIT)
                {
                    String itemID  = data.getStringExtra(
                            EditItemActivity.KEY_SHOPPINGITEM);

                    shoppinglistRecycleAdapter.updateShoppingItem(itemID, positionToEdit);
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.night_mode:
            {
                Toast.makeText(this, "Night mode", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.delete_all:
            {
                Toast.makeText(this, "Delete list", Toast.LENGTH_LONG).show();
                break;
            }
        }
        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }

}
