package com.oasisfeng.android.databinding.recyclerview;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableList;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Binding adapters for RecyclerView.
 *
 * Created by Oasis on 2016/2/14.
 */
@SuppressWarnings("unused")
public class RecyclerViewBindingAdapter {

	@BindingAdapter({"items", "item_binder", "item_layout_selector"})
	public static <T> void setItemsAndBinder(final RecyclerView recycler_view, final ObservableList<T> items, final ItemBinder<T> binder, final LayoutSelector<T> layout_selector) {
		recycler_view.setAdapter(new BindingRecyclerViewAdapter<>(items, binder, layout_selector));
	}

	@BindingAdapter({"items", "item_binder", "item_layout"})
	public static <T> void setItemsAndBinder(final RecyclerView recycler_view, final ObservableList<T> items, final ItemBinder<T> binder, final @LayoutRes int item_layout) {
		recycler_view.setAdapter(new BindingRecyclerViewAdapter<>(items, binder, new LayoutSelector<T>() {
			@Override public int getLayoutRes(final T model) { return item_layout; }
		}));
	}

	@BindingAdapter({"item_touch"})
	public static void setItemTouchHelper(final RecyclerView view, final ItemTouchHelper helper) {
		for (int i = 0;; i ++) try {
			final RecyclerView.ItemDecoration decoration = view.getItemDecorationAt(i);
			if (decoration == null) break;								// Null is returned on RecyclerView library 27+
			if (decoration == helper) return;
		} catch (final IndexOutOfBoundsException ignored) { break; }	// IndexOutOfBoundsException is thrown on RecyclerView library prior to 27.
		helper.attachToRecyclerView(view);
	}
}
