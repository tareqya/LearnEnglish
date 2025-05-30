package com.example.learnenglish.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.learnenglish.R;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
        private List<LeaderboardModel> list;
        private  Context activity;
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent,
        int viewType){

                // إنشاء "View" من ملف التصميم item.xml
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

                // إعادة الكائن ViewHolder الذي يحمل هذا الـ View
                return new ViewHolder(view);


        }

        @Override
        public void onBindViewHolder (@NonNull LeaderboardAdapter.ViewHolder holder,int position){

            LeaderboardModel model = list.get(position);
            holder.username.setText(model.username);
            holder.score.setText("Score: " + model.score);
            holder.tvPosition.setText(position + 1 + "");
            if(model.isCurrentUser){
                holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.green));
            }

        }

    @Override
    public int getItemCount () {
        return list.size();
    }

    public interface ItemSelected{
        void onItemClicked(int Index);
    }
    public LeaderboardAdapter(Context context, ArrayList<LeaderboardModel> list){
        this.list=list;
        activity=context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, score, tvPosition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username =itemView.findViewById(R.id.tvUsername);
            score=itemView.findViewById(R.id.tvScore);
            tvPosition = itemView.findViewById(R.id.tvPosition);
        }
    }


}