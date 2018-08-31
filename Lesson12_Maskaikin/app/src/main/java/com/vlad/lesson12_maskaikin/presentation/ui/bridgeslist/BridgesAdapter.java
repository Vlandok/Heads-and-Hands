package com.vlad.lesson12_maskaikin.presentation.ui.bridgeslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.vlad.lesson12_maskaikin.R;
import com.vlad.lesson12_maskaikin.data.model.Bridge;

import static com.vlad.lesson12_maskaikin.data.model.BridgeExtraKt.getImageStatusBridge;
import static com.vlad.lesson12_maskaikin.data.model.BridgeExtraKt.getTimeCloseBridge;


public class BridgesAdapter extends RecyclerView.Adapter<BridgesAdapter.BridgeViewHolder> {

    private List<Bridge> bridges = new ArrayList<>();
    private OnBridgeClickListener bridgeListener;

    interface OnBridgeClickListener {
       void onClickBridge(Bridge bridge);
    }

    public void setOnBridgeClickListener(OnBridgeClickListener listener) {
        bridgeListener = listener;
    }


    @Override
    public BridgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BridgeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bridge, parent, false), bridgeListener , bridges);
    }

    @Override
    public void onBindViewHolder(BridgeViewHolder holder, int position) {
        holder.bind(bridges.get(position));
    }

    @Override
    public int getItemCount() {
        return bridges.size();
    }

    public void setBridges(List<Bridge> bridges) {
        this.bridges = bridges;
        notifyDataSetChanged();
    }

    static class BridgeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewStatusBridge;
        private TextView textViewNameBridge;
        private TextView textViewCloseTimeBridge;
        private ImageView imageViewStatusAlarm;

        public BridgeViewHolder(View itemView, OnBridgeClickListener listener, List<Bridge> bridges) {
            super(itemView);
            this.imageViewStatusBridge = itemView.findViewById(R.id.imageViewStatusBridge);
            this.textViewNameBridge = itemView.findViewById(R.id.textViewNameBridge);
            this.textViewCloseTimeBridge = itemView.findViewById(R.id.textViewCloseTimeBridge);
            this.imageViewStatusAlarm = itemView. findViewById(R.id.imageViewStatusAlarm);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    Integer position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClickBridge(bridges.get(position));
                    }
                }
            });
        }

        public void bind(Bridge bridge)
        {
            imageViewStatusBridge.setBackgroundResource(getImageStatusBridge(bridge));
            textViewNameBridge.setText(bridge.getName()
                    .replace(" мост", "")
                    .replace("Мост ", ""));
            textViewCloseTimeBridge.setText(getTimeCloseBridge(bridge));
            imageViewStatusAlarm.setBackgroundResource(R.drawable.ic_kolocol_on);
        }

    }
}
