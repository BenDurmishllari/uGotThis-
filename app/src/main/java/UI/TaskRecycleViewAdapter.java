package UI;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Task;
import Util.TaskApi;


public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<Task> taskList;

    AlertDialog itemsDialog;

    public TaskRecycleViewAdapter(Context context, List<Task> taskList)
    {
        this.context = context;
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public TaskRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Working Part before test
//        View view = LayoutInflater.from(context).inflate(R.layout.tasks_for_list_view, parent, false);
//        return new ViewHolder(view, context);
        View view = LayoutInflater.from(context).inflate(R.layout.tasks_for_list_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view, context);




        viewHolder.itemsCardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemsDialog = new AlertDialog.Builder(context)
                                        .setIcon(android.R.drawable.ic_menu_edit)
                                        .setTitle("Manage Your Task" + TaskApi.getInstance().getUsername())
                                        .setMessage("Are you sure")
                                        .setPositiveButton(Html.fromHtml("<font color = '#0083FF'> Edit </font>"),
                                                            new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                            }
                                        })
                                        .setNeutralButton(Html.fromHtml("<font color = '#ff0000'> View Task </font>") , null)
                                        .setNegativeButton(Html.fromHtml("<font color = '#ff0000'> Delete </font>") , null)
                                        .show();

                Toast.makeText(context, "Test if its work" + String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
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
        private CardView itemsCardView;

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
            itemsCardView = itemView.findViewById(R.id.cardListTask);
        }
    }
}
