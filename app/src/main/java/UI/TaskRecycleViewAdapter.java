package UI;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ugotthis.Edit_activity;
import com.example.ugotthis.R;
import com.example.ugotthis.TaskList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Task;
import Util.TaskApi;

import static android.content.ContentValues.TAG;


public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<Task> taskList;
    Task task;
    public static String TaskDocId = "";

    AlertDialog itemsDialog;

    // Firebase attributes
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private static FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private RecyclerView recyclerView;
    private TaskRecycleViewAdapter taskRecycleViewAdapter;

    private CollectionReference collectionReference = database.collection("Task");
    public static DocumentReference docRef;

    private TaskListener mTaskListener;

    public interface  TaskListener
    {
        void onTaskComplete(int taskPosition);
    }



    public TaskRecycleViewAdapter(Context context, List<Task> taskList, TaskListener taskListener)
    {
        this.context = context;
        this.taskList = taskList;
        mTaskListener = taskListener;
    }

    @NonNull
    @Override
    public TaskRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(context).inflate(R.layout.tasks_for_list_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view, context);


        viewHolder.itemsCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mTaskListener.onTaskComplete(viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull TaskRecycleViewAdapter.ViewHolder holder, int position)
    {
        Task task = taskList.get(position);
        String imageUrl;

        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.status.setText(task.getStatus());
        imageUrl = task.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(task.getTimeAdded().getSeconds() * 1000);
        holder.dateAdded.setText(timeAgo);

        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.row_list_view)
                .fit()
                .into(holder.image);
    }

    @Override
    public int getItemCount() { return taskList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // test for taking id of the items
        public CardView itemsCardView;

        public TextView title,
                        description,
                        dateAdded,
                        name,
                        status;
        public ImageView image;
        String  userId,
                username;


        public ViewHolder(@NonNull View itemView, Context cntx)
        {
            super(itemView);
            context = cntx;
            title = itemView.findViewById(R.id.lblRowListTaskTitle);
            description = itemView.findViewById(R.id.lblRowListTaskDescription);
            dateAdded = itemView.findViewById(R.id.lblRowListTaskTimeStamp);
            image = itemView.findViewById(R.id.ImageViewRowListTasks);
            itemsCardView = itemView.findViewById(R.id.cardListTask);
            status = itemView.findViewById(R.id.lblStatus);
        }
    }
}
