package cm.architecture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteHolder> {
   
   public On_ItemClickListener listener;
   
   public NoteAdapter( ) {
      super(DIFF_CALLBACK);
   }
   
   private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
      @Override
      public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
         return oldItem.getId() == newItem.getId();
      }
   
      @Override
      public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
         return oldItem.getTitle().equals(newItem.getTitle()) &&
                  oldItem.getDescription().equals(newItem.getDescription()) &&
             oldItem.getPriority() == newItem.getPriority();
      }
   };
   
   @NonNull
   @Override
   public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemview = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.note_item, parent, false);
      return new NoteHolder(itemview);
   }
   
   @Override
   public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
      Note currentNote = getItem(position);
       holder.tv_title.setText(currentNote.getTitle());
          holder.tv_description.setText(currentNote.getDescription());
                holder.tv_priority.setText(String.valueOf(currentNote.getPriority()));
   }
   
   public Note getNoteAt(int position){
      return getItem(position);
   }
   
   class NoteHolder extends RecyclerView.ViewHolder{
      private TextView tv_title, tv_priority, tv_description;
   
      public NoteHolder(@NonNull View itemView) {
         super(itemView);
         tv_title = itemView.findViewById(R.id.tv_title);
         tv_priority = itemView.findViewById(R.id.tv_priority);
         tv_description = itemView.findViewById(R.id.tv_description);
         itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int position = getAdapterPosition();
               if (listener!=null && position != RecyclerView.NO_POSITION){
               listener.on_ItemClick(getItem(position));}
            }
         });
      }
   }
   
   public interface On_ItemClickListener {
      void on_ItemClick(Note note);
   }
   
   public void set_OnItemClickListener(On_ItemClickListener listener){
   this.listener = listener;
   }
}
