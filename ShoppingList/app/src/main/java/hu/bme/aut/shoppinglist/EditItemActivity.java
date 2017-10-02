package hu.bme.aut.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import hu.bme.aut.shoppinglist.data.ShoppingItem;
import io.realm.Realm;

public class EditItemActivity extends AppCompatActivity
{
    public static final String KEY_SHOPPINGITEM = "KEY_SHOPPINGITEM";
    private EditText etShoppingItemText;
    private CheckBox cbShoppingItemStatus;
    private ShoppingItem shoppingItemToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shoppingitem);

        if (getIntent().hasExtra(MainActivity.KEY_SHOPPINGITEM_ID))
        {
            String todoID = getIntent().getStringExtra(MainActivity.KEY_SHOPPINGITEM_ID);
            shoppingItemToEdit = getRealm().where(ShoppingItem.class)
                    .equalTo("itemID", todoID)
                    .findFirst();
        }

        etShoppingItemText = (EditText) findViewById(R.id.etShoppingItemText);
        cbShoppingItemStatus = (CheckBox) findViewById(R.id.cbShoppingItemStatus);

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
            etShoppingItemText.setError("can not be empty");
        } else {
            Intent intentResult = new Intent();

            getRealm().beginTransaction();
            shoppingItemToEdit.setName(etShoppingItemText.getText().toString());
            shoppingItemToEdit.setStatus(cbShoppingItemStatus.isChecked());
            getRealm().commitTransaction();

            intentResult.putExtra(KEY_SHOPPINGITEM, shoppingItemToEdit.getItemID());
            setResult(RESULT_OK, intentResult);
            finish();
        }
    }
}
