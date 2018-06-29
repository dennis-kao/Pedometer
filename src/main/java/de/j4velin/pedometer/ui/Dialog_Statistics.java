/*
 * Copyright 2014 Thomas Hoffmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.j4velin.pedometer.ui;

import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.util.Util;

/* Keeps track of the steps taken by the user and exact date and time (from Util class)
 * and sends them to fragment_Overview to update the graph and figures of steps 
 * taken by the user
 */
abstract class Dialog_Statistics {

	public static Dialog getDialog(final Context c, int since_boot) {
		final Dialog d = new Dialog(c);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setContentView(R.layout.statistics);
		d.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		Database db = Database.getInstance(c);

		//gets the current month, week, date and time 
		Pair<Date, Integer> record = db.getRecordData();

		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(Util.getToday());
		int daysThisMonth = date.get(Calendar.DAY_OF_MONTH);

		date.add(Calendar.DATE, -6);

		int thisWeek = db.getSteps(date.getTimeInMillis(), System.currentTimeMillis()) + since_boot;

		date.setTimeInMillis(Util.getToday());
		date.set(Calendar.DAY_OF_MONTH, 1);
		int thisMonth = db.getSteps(date.getTimeInMillis(), System.currentTimeMillis()) + since_boot;

		((TextView) d.findViewById(R.id.record)).setText(
				Statistics_Fragment.formatter.format(record.second) + " @ "
				+ java.text.DateFormat.getDateInstance().format(record.first));

		//calling fragment_Overview function so time and user's activity could be updated on the app
		((TextView) d.findViewById(R.id.totalthisweek)).setText(Statistics_Fragment.formatter.format(thisWeek));
		((TextView) d.findViewById(R.id.totalthismonth)).setText(Statistics_Fragment.formatter.format(thisMonth));

		((TextView) d.findViewById(R.id.averagethisweek)).setText(Statistics_Fragment.formatter.format(thisWeek / 7));
		((TextView) d.findViewById(R.id.averagethismonth)).setText(Statistics_Fragment.formatter.format(thisMonth / daysThisMonth));
		
		db.close();
		
		return d;
	}

}
