package com.example.myapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ListViewBinding;
import com.example.myapplication.databinding.LoadingProgressBinding;
import com.example.myapplication.dataclass.UserData;

import java.util.List;

public class UserAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<UserData> data;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public UserAdapters(Context context, List<UserData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM:
                ListViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_view, parent, false);
                viewHolder = new DataHolder(binding);
                break;
            case LOADING:
                LoadingProgressBinding loadingProgressBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.loading_progress, parent, false);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserData userData = data.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                DataHolder dataHolder = (DataHolder) holder;
                dataHolder.binding.idText.setText(String.valueOf(userData.getId()));
                dataHolder.binding.titleText.setText(userData.getTitle());
                dataHolder.binding.bodyText.setText(userData.getBody());
                break;
            case LOADING:
                LoadingHolder holder1 = (LoadingHolder) holder;
                holder1.loadingProgressBinding.ldProgressBar.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0: data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == data.size() -1 && isLoadingAdded) ? LOADING:ITEM;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UserData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = data.size() - 1;
        UserData result = getItem(position);

        if (result != null) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(UserData movie) {
        data.add(movie);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<UserData> moveResults) {
        for (UserData result : moveResults) {
            add(result);
        }
    }

    public UserData getItem(int position) {
        return data.get(position);
    }

    public static class DataHolder extends RecyclerView.ViewHolder {
        public ListViewBinding binding;

        public DataHolder(ListViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingProgressBinding loadingProgressBinding;

        public LoadingHolder(LoadingProgressBinding itemView) {
            super(itemView.getRoot());
            this.loadingProgressBinding = itemView;
        }
    }
}
