package com.vlad.lesson4_maskaikin.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vlad.lesson4_maskaikin.BaseInfoItem;
import com.vlad.lesson4_maskaikin.DetailInfoItem;
import com.vlad.lesson4_maskaikin.R;

import java.util.ArrayList;


public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_BASE_ITEM = 0;
    private final static int TYPE_DETAIL_ITEM = 1;
    private final static int TYPE_DETAIL_ALL_WIDTH_ITEM = 2;
    private ViewHolderBase viewHolderBase;
    private ViewHolderDetail viewHolderDetail;
    private ViewHolderDetailAllWidth viewHolderDetailAllWidth;
    private ArrayList<BaseInfoItem> itemsBase;
    private ArrayList<DetailInfoItem> itemsDetail;
    private OnItemClickListener itemListener;


    public interface OnItemClickListener {
        void onClickItem(BaseInfoItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemListener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        if (position > itemsDetail.size() - 1) {
            return 0;
        } else if (itemsDetail.size() % 2 != 0 && position % (itemsDetail.size() - 1) == 0 && position != 0) {
            return 2;
        } else return 1;

    }

    public int getSpanSize(int position) {
        return (position > (itemsDetail.size() - 1) || (itemsDetail.size() % 2 != 0 && position % (itemsDetail.size() - 1) == 0 && position != 0) ? 2 : 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == TYPE_BASE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_item_view, parent, false);
            viewHolderBase = new ViewHolderBase(view, itemListener);
            return viewHolderBase;
        } else if (viewType == TYPE_DETAIL_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_view, parent, false);
            viewHolderDetail = new ViewHolderDetail(view, itemListener);
            return viewHolderDetail;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_all_width_view, parent, false);
            viewHolderDetailAllWidth = new ViewHolderDetailAllWidth(view, itemListener);
            return viewHolderDetailAllWidth;
        }
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_BASE_ITEM) {

            if (position > itemsDetail.size() - 1) {
                position = position - itemsDetail.size();
            }

            viewHolderBase = (ViewHolderBase) holder;
            viewHolderBase.appCompatImageViewBase.setBackgroundResource(itemsBase.get(position).icon);
            viewHolderBase.textViewTitleBase.setText(itemsBase.get(position).title);

        } else if (getItemViewType(position) == TYPE_DETAIL_ITEM) {
            viewHolderDetail = (ViewHolderDetail) holder;
            viewHolderDetail.appCompatImageViewDetail.setBackgroundResource(itemsDetail.get(position).icon);
            viewHolderDetail.textViewTitleDetail.setText(itemsDetail.get(position).title);
            viewHolderDetail.textViewDescriptionDetail.setText(itemsDetail.get(position).description);
            if (itemsDetail.get(position).getNeedIntent()) {
                viewHolderDetail.textViewDescriptionDetail.setTextColor(ContextCompat.getColor(viewHolderDetail.textViewDescriptionDetail.getContext(), R.color.coral));
            }
        } else if (getItemViewType(position) == TYPE_DETAIL_ALL_WIDTH_ITEM) {
            viewHolderDetailAllWidth = (ViewHolderDetailAllWidth) holder;
            viewHolderDetailAllWidth.appCompatImageViewDetailAllWidth.setBackgroundResource(itemsDetail.get(position).icon);
            viewHolderDetailAllWidth.textViewTitleDetailAllWidth.setText(itemsDetail.get(position).title);
            viewHolderDetailAllWidth.textViewDescriptionDetailAllWidth.setText(itemsDetail.get(position).description);
            if (itemsDetail.get(position).getNeedIntent()) {
                viewHolderDetailAllWidth.textViewDescriptionDetailAllWidth.setTextColor(ContextCompat.getColor(viewHolderDetailAllWidth.textViewDescriptionDetailAllWidth.getContext(), R.color.coral));
            }
        }


    }

    @Override
    public int getItemCount() {
        return (itemsDetail.size() + itemsBase.size());
    }


    public class ViewHolderBase extends RecyclerView.ViewHolder {
        AppCompatImageView appCompatImageViewBase;
        TextView textViewTitleBase;

        ViewHolderBase(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitleBase = itemView.findViewById(R.id.textViewTitleBase);
            appCompatImageViewBase = itemView.findViewById(R.id.appCompactImageViewBase);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (position > itemsDetail.size() - 1) {
                                position = position - itemsDetail.size();
                                listener.onClickItem(itemsBase.get(position));
                            }
                        }
                    }
                }
            });
        }
    }

    public class ViewHolderDetail extends RecyclerView.ViewHolder {
        AppCompatImageView appCompatImageViewDetail;
        TextView textViewTitleDetail;
        TextView textViewDescriptionDetail;

        ViewHolderDetail(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitleDetail = itemView.findViewById(R.id.textViewTitleDetail);
            textViewDescriptionDetail = itemView.findViewById(R.id.textViewDescriptionDetail);
            appCompatImageViewDetail = itemView.findViewById(R.id.appCompactImageViewDetail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClickItem(itemsDetail.get(position));
                        }
                    }
                }
            });

        }
    }

    public class ViewHolderDetailAllWidth extends RecyclerView.ViewHolder {
        AppCompatImageView appCompatImageViewDetailAllWidth;
        TextView textViewTitleDetailAllWidth;
        TextView textViewDescriptionDetailAllWidth;

        ViewHolderDetailAllWidth(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewTitleDetailAllWidth = itemView.findViewById(R.id.textViewTitleDetailAllWidth);
            textViewDescriptionDetailAllWidth = itemView.findViewById(R.id.textViewDescriptionDetailAllWidth);
            appCompatImageViewDetailAllWidth = itemView.findViewById(R.id.appCompactImageViewDetailAllWidth);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClickItem(itemsDetail.get(position));
                        }
                    }
                }
            });
        }
    }

    public RVAdapter(){

    }

    public RVAdapter(ArrayList<DetailInfoItem> itemsDetail, ArrayList<BaseInfoItem> itemsBase) {
        this.itemsDetail = itemsDetail;
        this.itemsBase = itemsBase;
    }

}
