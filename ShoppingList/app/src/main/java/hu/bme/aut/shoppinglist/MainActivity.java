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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
    protected void onCreate(Bundle savedInstanceState)
    {
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

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etShoppingItemText = new EditText(this);
        etShoppingItemText.setHint("Item name");
        layout.addView(etShoppingItemText);

        final String[] categorySelected = new String[1];
        final int[] categoryID = new int[1];
        final Spinner categorySpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                categorySelected[0] = (String)adapterView.getItemAtPosition(i);
                categoryID[0] = (int)(long)adapterView.getItemIdAtPosition(i) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        layout.addView(categorySpinner);

        final EditText etShoppingItemPrice = new EditText(this);
        etShoppingItemPrice.setHint("Estimated price");
        layout.addView(etShoppingItemPrice);

        final EditText etShoppingItemAmount = new EditText(this);
        etShoppingItemAmount.setHint("Amount required");
        layout.addView(etShoppingItemAmount);

        final EditText etShoppingItemDescription = new EditText(this);
        etShoppingItemDescription.setHint("Item description");
        layout.addView(etShoppingItemDescription);


        builder.setView(layout) ;



        builder.setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String name = etShoppingItemText.getText().toString();
                String description = etShoppingItemDescription.getText().toString();

                if("".equals(name))
                {
                    etShoppingItemText.setError("Can not be empty");
                    Toast.makeText(MainActivity.this, "Item name cannot be empty", Toast.LENGTH_LONG).show();
                }
                else
                {
                    try
                    {
                        int price = Integer.parseInt(etShoppingItemPrice.getText().toString());
                        int amount = Integer.parseInt(etShoppingItemAmount.getText().toString()) ;

                        shoppinglistRecycleAdapter.addShoppingItem(name, categorySelected[0], categoryID[0], price, amount, description);
                        recyclerShoppinglist.scrollToPosition(0);

                        Toast.makeText(MainActivity.this, "Item added", Toast.LENGTH_LONG).show();
                    }
                    catch(NumberFormatException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Enter an integer value for the price/amount", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
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
            case R.id.delete_all:
            {
                shoppinglistRecycleAdapter.deleteShoppingList();
                Toast.makeText(this, "List deleted", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.total_cost:
            {
                Toast.makeText(MainActivity.this, "Estimated cost: " + String.valueOf(shoppinglistRecycleAdapter.totalCost()) + " HUF", Toast.LENGTH_LONG).show();
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
