package com.oasisfeng.android.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import androidx.annotation.Nullable;

/**
 * Utilities for {@link Activity}
 *
 * Created by Oasis on 2016/2/24.
 */
public class Activities {

	public static @Nullable Activity findActivityFrom(final Context context) {
		if (context instanceof Activity) return (Activity) context;
		if (context instanceof Application || context instanceof Service) return null;
		if (! (context instanceof ContextWrapper)) return null;
		final Context base_context = ((ContextWrapper) context).getBaseContext();
		if (base_context == context) return null;
		return findActivityFrom(base_context);
	}

	public static void startActivity(final Context context, final Intent intent) {
		final Activity activity = findActivityFrom(context);
		if (activity != null) activity.startActivity(intent);
		else context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
}
