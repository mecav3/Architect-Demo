package cm.architecture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    NoteViewModel noteViewModel;
   FloatingActionButton fab_add;
   public static final int ADD_NOTE_REQUEST = 1;
   public static final int EDIT_NOTE_REQUEST = 2;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      fab_add = findViewById(R.id.btn_add);
      fab_add.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivityForResult(i,ADD_NOTE_REQUEST);
         }
      });
      
      RecyclerView recyclerView = findViewById(R.id.rv_list);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setHasFixedSize(true);
      
      NoteAdapter adapter = new NoteAdapter();
      recyclerView.setAdapter(adapter);
      
      noteViewModel = new ViewModelProvider(this,
          ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
      
      noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
         @Override
         public void onChanged(List<Note> notes) {
            
            Toast.makeText(MainActivity.this, "on change"+notes.isEmpty(), Toast.LENGTH_SHORT).show();
            adapter.submitList(notes);
         }
      });
      
//      noteViewModel.getAllNotes().observe(this, notes ->
//              Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//          adapter.submitList(notes));
      
      new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
          ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
         @Override
         public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
         }
         @Override
         public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            noteViewModel.delete( adapter.getNoteAt( viewHolder.getAdapterPosition() ));
            Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
         }
      }).attachToRecyclerView(recyclerView);
      
      adapter.set_OnItemClickListener(new NoteAdapter.On_ItemClickListener() {
         @Override
         public void on_ItemClick(Note note) {
            Intent i = new Intent(MainActivity.this, AddEditNoteActivity.class);
            i.putExtra(AddEditNoteActivity.mEXTRA_ID, note.getId());
             i.putExtra(AddEditNoteActivity.mEXTRA_TITLE, note.getTitle());
            i.putExtra(AddEditNoteActivity.mEXTRA_DESCRIPTION, note.getDescription());
            i.putExtra(AddEditNoteActivity.mEXTRA_PRIORITY, note.getPriority());
          startActivityForResult(i,EDIT_NOTE_REQUEST);
         }
      });
      
   }
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      
      if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
         String title = data.getStringExtra(AddEditNoteActivity.mEXTRA_TITLE);
         String description = data.getStringExtra(AddEditNoteActivity.mEXTRA_DESCRIPTION);
         int priority = data.getIntExtra(AddEditNoteActivity.mEXTRA_PRIORITY,1);
               Note note = new Note(title, description, priority);
             noteViewModel.insert(note);
         Toast.makeText(this, "note saved", Toast.LENGTH_SHORT).show();
      }else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
         
         int id = data.getIntExtra(AddEditNoteActivity.mEXTRA_ID,-1);
         
         if (id == -1){
            Toast.makeText(this, "note cant be updated", Toast.LENGTH_SHORT).show();
            return;
         }
         
         String title = data.getStringExtra(AddEditNoteActivity.mEXTRA_TITLE);
         String description = data.getStringExtra(AddEditNoteActivity.mEXTRA_DESCRIPTION);
         int priority = data.getIntExtra(AddEditNoteActivity.mEXTRA_PRIORITY,1);
               Note note = new Note(title, description, priority);
               note.setId(id);
             noteViewModel.update(note);
         Toast.makeText(this, "note updated", Toast.LENGTH_SHORT).show();
      }
         else {
         Toast.makeText(this, "note not saved", Toast.LENGTH_SHORT).show();
      }
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.main_menu,menu);
      return true;
   }
   
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()){
         case R.id.delete_all:
          noteViewModel.deleteAllNotes();
            Toast.makeText(MainActivity.this, "All Deleted", Toast.LENGTH_SHORT).show();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }
}