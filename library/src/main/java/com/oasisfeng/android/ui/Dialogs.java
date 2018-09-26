package com.oasisfeng.android.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/** @author Oasis */
public class Dialogs {

	/** Create an non-cancellable alert dialog builder. */
	public static @CheckResult Builder buildAlert(final Activity activity, final @StringRes int title, final @StringRes int message) {
		return buildAlert(activity, title != 0 ? activity.getText(title) : null, message != 0 ? activity.getText(message) : null);
	}

	public static @CheckResult Builder buildAlert(final Activity activity, final @Nullable CharSequence title, final @Nullable CharSequence message) {
		final Builder builder = new Builder(activity);
		builder.setCancelable(true);
		if (title != null) builder.setTitle(title);
		if (message != null) builder.setMessage(message);
		return builder;
	}

	/** Provide shortcuts for simpler building */
	public static class Builder extends AlertDialog.Builder {

		public @CheckResult Builder withOkButton(final @Nullable Runnable task) {
			setPositiveButton(android.R.string.ok, task == null ? null : new DialogInterface.OnClickListener() { @Override public void onClick(final DialogInterface dialog, final int which) {
				task.run();
			}});
			return this;
		}

		public @CheckResult Builder withCancelButton() {
			setNegativeButton(android.R.string.cancel, null);
			return this;
		}

		public Builder(final Context context) { super(context); }
	}
}
