package com.example.myway.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myway.R;
import com.example.myway.models.Data;
import com.example.myway.models.Document;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Data> mArrayList;
    private String TAG = "Adapter";


    public Adapter(Context context, ArrayList<Data> arrayList) {
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
        View view = inflater.inflate(R.layout.activity_notice_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Data data = mArrayList.get(position);

        holder.notice_title.setText(data.getTitle());
        holder.notice_date.setText(data.getDate());

    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"getItemCount" + mArrayList.size());
        return mArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notice_title,notice_date;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            notice_title = itemView.findViewById(R.id.notice_title);
            notice_date = itemView.findViewById(R.id.notice_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION) {
                        if(mListener !=null) {
                            mListener.onItemClick(view,position);
                        }
                    }
                }
            });
        }

    }
}