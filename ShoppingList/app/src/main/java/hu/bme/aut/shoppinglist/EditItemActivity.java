package hu.bme.aut.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import hu.bme.aut.shoppinglist.data.ShoppingItem;
import io.realm.Realm;

public class EditItemActivity extends AppCompatActivity
{
    public static final String KEY_SHOPPINGITEM = "KEY_SHOPPINGITEM";
    private EditText etShoppingItemText;
    private Spinner categorySpinner;
    private EditText etShoppingItemPrice;
    private EditText etEtShoppingItemAmount;
    private EditText etShoppingItemDescription;
    private CheckBox cbShoppingItemStatus;
    private ShoppingItem shoppingItemToEdit = null;
    private String categorySelected;
    private int categorySelectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shoppingitem);

        if (getIntent().hasExtra(MainActivity.KEY_SHOPPINGITEM_ID))
        {
            String itemID = getIntent().getStringExtra(MainActivity.KEY_SHOPPINGITEM_ID);
            shoppingItemToEdit = getRealm().where(ShoppingItem.class)
                    .equalTo("itemID", itemID)
                    .findFirst();
        }

        etShoppingItemText = (EditText) findViewById(R.id.etShoppingItemText);
        categorySpinner = (Spinner) findViewById(R.id.spinner);
        etShoppingItemPrice = (EditText) findViewById(R.id.etShoppingItemPrice);
        etShoppingItemDescription = (EditText) findViewById(R.id.etShoppingItemDescription);
        cbShoppingItemStatus = (CheckBox) findViewById(R.id.cbShoppingItemStatus);
        etEtShoppingItemAmount = (EditText) findViewById(R.id.etShoppingItemAmount);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                categorySelected = new String((String)adapterView.getItemAtPosition(i)) ;
                categorySelectedID = (int)(long) adapterView.getItemIdAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                saveShoppingItem();
            }
        });

        if (shoppingItemToEdit != null)
        {
            etShoppingItemText.setText(shoppingItemToEdit.getName());
            categorySpinner.setSelection(shoppingItemToEdit.getCategoryID());
            etShoppingItemPrice.setText(String.valueOf(shoppingItemToEdit.getPrice()));
            etEtShoppingItemAmount.setText(String.valueOf(shoppingItemToEdit.getAmount()));
            etShoppingItemDescription.setText(shoppingItemToEdit.getDescription());
            cbShoppingItemStatus.setChecked(shoppingItemToEdit.getStatus());
        }
    }

    public Realm getRealm()
    {
        return ((MainApplication)getApplication()).getRealmItem();
    }

    private void saveShoppingItem()
    {
        if ("".equals(etShoppingItemText.getText().toString()))
        {
            etShoppingItemText.setError("Can not be empty");
        }
        else
        {
            try
            {
                Intent intentResult = new Intent();

                getRealm().beginTransaction();
                shoppingItemToEdit.setName(etShoppingItemText.getText().toString());
                shoppingItemToEdit.setStatus(cbShoppingItemStatus.isChecked());
                shoppingItemToEdit.setDescription(etShoppingItemDescription.getText().toString());
                shoppingItemToEdit.setPrice(Integer.parseInt(etShoppingItemPrice.getText().toString()));
                shoppingItemToEdit.setAmount(Integer.parseInt(etEtShoppingItemAmount.getText().toString()));
                shoppingItemToEdit.setCategory(categorySelected);
                shoppingItemToEdit.setCategoryID(categorySelectedID);
                getRealm().commitTransaction();

                intentResult.putExtra(KEY_SHOPPINGITEM, shoppingItemToEdit.getItemID());
                setResult(RESULT_OK, intentResult);
                finish();
            }
            catch(NumberFormatException e)
            {
                e.printStackTrace();
                etShoppingItemPrice.setError("Enter integer value");
                etEtShoppingItemAmount.setError("Enter integer value");
            }
        }
    }
}
