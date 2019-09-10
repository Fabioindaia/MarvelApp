package com.uolinc.marvelapp.ui.characterlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uolinc.marvelapp.R;
import com.uolinc.marvelapp.model.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private List<Result> resultList;
    private Context context;
    private final int VIEW_TYPE_LOADING = 0;

    CharacterAdapter(List<Result> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i == VIEW_TYPE_LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_loading, parent, false));
        }else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_character, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, final int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_ITEM = 1;
        return position == resultList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class ViewHolder extends CharacterViewHolder {

        private SimpleDraweeView imgCharacter;
        private TextView txtName;
        private TextView txtDescription;

        ViewHolder(View view) {
            super(view);
            imgCharacter = view.findViewById(R.id.imgCharacter);
            txtName = view.findViewById(R.id.txtName);
            txtDescription = view.findViewById(R.id.txtDescription);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            Result result = resultList.get(position);
            String urlImage = result.getThumbnail().getPath() + "." + result.getThumbnail().getExtension();

            imgCharacter.setImageURI(urlImage);
            txtName.setText(result.getName());
            txtDescription.setText(result.getDescription());

            itemView.setOnClickListener((View v) -> {
                MainContrato.View view = (MainContrato.View) context;
                view.showCharacterDetailActivity(result, urlImage);
            });
        }
    }

    public class LoadingViewHolder extends CharacterViewHolder {

        @BindView(R.id.progressBarLoading)
        ProgressBar progressBarLoading;

        LoadingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        protected void clear() {

        }
    }

    public abstract class CharacterViewHolder extends RecyclerView.ViewHolder{

        private CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected abstract void clear();

        public void onBind(int position) {
            clear();
        }

    }
}
