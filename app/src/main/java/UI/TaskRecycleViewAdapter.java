package UI;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ugotthis.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Task;


public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<Task> taskList;

    public TaskRecycleViewAdapter(Context context, List<Task> taskList)
    {
        this.context = context;
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public TaskRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(context).inflate(R.layout.tasks_for_list_view, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecycleViewAdapter.ViewHolder holder, int position)
    {
        Task task = taskList.get(position);
        String imageUrl;

        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
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
        public TextView title,
                        description,
                        dateAdded,
                        name;
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
        }
    }
}
