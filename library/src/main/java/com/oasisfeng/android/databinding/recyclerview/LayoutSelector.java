package com.oasisfeng.android.databinding.recyclerview;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The layout selector interface for items in {@link RecyclerView}.
 *
 * Created by Oasis on 2017/1/16.
 */
public interface LayoutSelector<T> {
	@LayoutRes int getLayoutRes(T model);
}
