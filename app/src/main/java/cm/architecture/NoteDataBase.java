package cm.architecture;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class, version = 1 )
public abstract class NoteDataBase extends RoomDatabase {
   
   private static NoteDataBase instance;
   
   public abstract NoteDao noteDao();
   
   public static synchronized NoteDataBase getInstance(Context context){
      if (instance==null){
         instance  = Room.databaseBuilder(context.getApplicationContext(),
              NoteDataBase.class,"note_database")
             .addCallback(roomCallback)
             .fallbackToDestructiveMigration()
             .build();
      }
      return instance;
   }
   
   private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {
         super.onCreate(db);
         new PopulateDBAsyncTask(instance).execute();
      }
   };
   
   private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
     private NoteDao noteDao;
     
      private PopulateDBAsyncTask(NoteDataBase db) {
         noteDao = db.noteDao();
      }
   
      @Override
      protected Void doInBackground(Void... voids) {
         noteDao.insert( new Note("title_1","Description_1",1) );
         noteDao.insert( new Note("title_2","Description_2",2) );
         noteDao.insert( new Note("title_3","Description_3",3) );
         noteDao.insert( new Note("title_4","Description_1",4) );
         noteDao.insert( new Note("title_5","Description_2",2) );
         noteDao.insert( new Note("title_6","Description_3",3) );
         noteDao.insert( new Note("title_7","Description_1",1) );
         noteDao.insert( new Note("title_8","Description_2",2) );
         noteDao.insert( new Note("title_9","Description_3",3) );
         noteDao.insert( new Note("title_91","Description_1",1) );
         noteDao.insert( new Note("title_92","Description_2",2) );
         noteDao.insert( new Note("title_93","Description_3",3) );
         return null;
      }
   }
}
