package net.admission.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

import net.admission.R;
import net.admission.model.Transact;
import net.admission.utils.ItemAnimation;

public class AdapterListAnimation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Transact> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnItemDeleteListener mOnItemDestroyListener;
    private int animation_type = 0;
    private boolean add_first = false;
    private boolean btn_remove = true;

    public interface OnItemClickListener {
        void onItemClick(View view, Transact obj, int position);
    }

    public interface OnItemDeleteListener {
        void onItemDelete(View view, Transact obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setmOnItemDestroyListener(final OnItemDeleteListener mOnItemDestroyListener) {
        this.mOnItemDestroyListener = mOnItemDestroyListener;
    }

    public AdapterListAnimation(Context context, List<Transact> items, int animation_type, boolean add_first) {
        this.items = items;
        ctx = context;
        this.animation_type = animation_type;
        this.add_first = add_first;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView datetime;
        public TextView detail;
        public RelativeLayout layoutParent;

        public OriginalViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            datetime = (TextView) v.findViewById(R.id.datetime);
            detail = (TextView) v.findViewById(R.id.detail);
            layoutParent = (RelativeLayout) v.findViewById(R.id.layout_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transact, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolder : " + position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Transact p = items.get(position);

            view.name.setText(p.getPassno());
            view.datetime.setText(p.getDatetime());
            //Tools.displayImageRound(ctx, view.image, p.image);
            view.layoutParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            setAnimation(view.itemView, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void isBtnRemove(Boolean remove){
        btn_remove = remove;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }

}