package com.example.myway.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myway.R;
import com.example.myway.models.Data;
import com.example.myway.models.UserListInfo;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserListInfo> mArrayList;
    private String TAG = "Adapter";


    public UserListAdapter(Context context, ArrayList<UserListInfo> arrayList) {
        this.context = context;
        this.mArrayList = arrayList;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_userlist_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserListInfo data = mArrayList.get(position);
        holder.userID.setText(data.getUser_id());
        holder.userEmail.setText(data.getUserEmail());
        holder.userName.setText(data.getUserName());

    }


    @Override
    public int getItemCount() {
        Log.d(TAG,"getItemCount" + mArrayList.size());
        return mArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userID, userEmail, userName;

        public ViewHolder(@NonNull final View itemView) {

            super(itemView);
            userID = itemView.findViewById(R.id.userID);
            userEmail = itemView.findViewById(R.id.userEmail);
            userName = itemView.findViewById(R.id.userName);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION) {
//                        mListener.onItemClick(view,pos);
//                    }
//                }
//            });

        }
    }

}