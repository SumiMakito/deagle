package com.oasisfeng.android.databinding.recyclerview;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The binder interface for items in {@link RecyclerView}.
 *
 * Created by Oasis on 2016/2/14.
 */
public interface ItemBinder<T> {
	void onBind(ViewDataBinding container, T model, ViewDataBinding item);
}
