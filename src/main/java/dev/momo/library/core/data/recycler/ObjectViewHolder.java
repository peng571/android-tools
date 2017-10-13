package dev.momo.library.core.data.recycler;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Peng on 2016/12/28.
 */

public abstract class ObjectViewHolder<T> extends RecyclerView.ViewHolder  {

    public static final String TAG = ObjectViewHolder.class.getSimpleName();

    private OnItemClickListener<T> clickListener;
    private OnItemLongClickListener<T> longClickListener;

    int index;
    T object;

    final private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (clickListener == null) return;
            clickListener.onItemClick(ObjectViewHolder.this, index, object);
        }
    };

    final private View.OnLongClickListener longListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (longClickListener == null) return true;
            longClickListener.onItemLongClick(ObjectViewHolder.this, index, object);
            return false;
        }
    };


    public ObjectViewHolder(View itemView) {
        super(itemView);
        setClickableItem(itemView);
        setLongClickableItem(itemView);
    }

    /**
     * Override this to change clickable view
     * use rootView as default clickable range
     *
     * @param view
     */
    public void setClickableItem(View view) {
        view.setOnClickListener(listener);
    }


    public void setLongClickableItem(View view) {
        view.setOnLongClickListener(longListener);
    }

    @CallSuper
    protected void onBind(int position, T object) {
        this.index = position;
        this.object = object;
    }

    public ObjectViewHolder<T> listen(OnItemClickListener<T> listener) {
        this.clickListener = listener;
        return this;
    }

    public ObjectViewHolder<T> listen(OnItemLongClickListener<T> listener) {
        this.longClickListener = listener;
        return this;
    }

    public abstract View getTransitionTarget();


}
