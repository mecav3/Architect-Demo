package cm.architecture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
   private EditText et_title, et_description;
   private NumberPicker numberPicker;
   
   public static final String mEXTRA_ID = " cm.architecture.ID";
   public static final String mEXTRA_TITLE = " cm.architecture.EXTRA_TITLE";
   public static final String mEXTRA_DESCRIPTION = " cm.architecture.EXTRA_DESCRIPTION";
   public static final String mEXTRA_PRIORITY = " cm.architecture.EXTRA_PRIORITY";
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_add_note);
      
      et_title = findViewById(R.id.et_title);
      et_description = findViewById(R.id.et_description);
      numberPicker = findViewById(R.id.number_picker);
      
      numberPicker.setMinValue(1);
      numberPicker.setMaxValue(10);
      
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
      
      Intent i = getIntent();
      if ( i.hasExtra(mEXTRA_ID) ) {
         setTitle("Edit Note");
         et_title.setText(i.getStringExtra(mEXTRA_TITLE));
         et_description.setText(i.getStringExtra(mEXTRA_DESCRIPTION));
         numberPicker.setValue(i.getIntExtra(mEXTRA_PRIORITY,1));
         
      }else{
      setTitle("Add Note");}
   }
   
   private void save_note() {
      String title = et_title.getText().toString();
      String description = et_description.getText().toString();
      int priority = numberPicker.getValue();
      
      if(title.trim().isEmpty() || description.trim().isEmpty()) {
         Toast.makeText(this, "please enter both data ", Toast.LENGTH_SHORT).show();
         return;
      }
      
      Intent i = new Intent();
      i.putExtra(mEXTRA_TITLE,title);
      i.putExtra(mEXTRA_DESCRIPTION,description);
      i.putExtra(mEXTRA_PRIORITY,priority);
      
      int id = getIntent().getIntExtra(mEXTRA_ID, -1);
      if (id != -1) {
         i.putExtra(mEXTRA_ID,id);
      }
      setResult(RESULT_OK,i);
      finish();
   }
   
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()){
         case R.id.save:
            save_note();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.add_note_menu,menu);
      return true;
   }
}