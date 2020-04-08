package com.lamzone.mareu;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.appcompat.widget.MenuPopupWindow;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class Tests_Instrumentalises {

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        mActivity = mActivityTestRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void addMeetingWithSuccess() {
        onView(ViewMatchers.withId(R.id.add_meeting_button)).perform(click());
        onView(ViewMatchers.withId(R.id.valider)).perform(click());
        onView(ViewMatchers.withId(R.id.Recyclerview)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void deleteItemWithSuccess() {
       onView(ViewMatchers.withId(R.id.item_list_delete_button)).perform(click());
       onView(ViewMatchers.withId(R.id.add_meeting_button)).perform(click());
       onView(ViewMatchers.withId(R.id.valider)).perform(click());
       onView(ViewMatchers.withId(R.id.item_list_delete_button)).perform(click());
       onView(ViewMatchers.withId(R.id.Recyclerview)).check(matches(hasMinimumChildCount(0)));
    }

    @Test
    public void filterByPlace() {
        //Ajoute une première réunion
        onView(ViewMatchers.withId(R.id.add_meeting_button)).perform(click());
        onView(ViewMatchers.withId(R.id.meetingPlace)).perform(typeText("Test"));
        onView(ViewMatchers.withId(R.id.valider)).perform(click());
        //Ajoute une deuxième réunion
        onView(ViewMatchers.withId(R.id.add_meeting_button)).perform(click());
        onView(ViewMatchers.withId(R.id.meetingPlace)).perform(typeText("Salle B"));
        onView(ViewMatchers.withId(R.id.valider)).perform(click());
        //Filtre les réunions
        onView(ViewMatchers.withId(R.id.filtrer)).perform(click());
        onData(CoreMatchers.anything())
                .inRoot(RootMatchers.isPlatformPopup())
                .inAdapterView(CoreMatchers.<View>instanceOf(MenuPopupWindow.MenuDropDownListView.class))
                .atPosition(1)
                .perform(click());
        onView(ViewMatchers.withId(R.id.trier_lieu)).perform(typeText("Te"));
        onView(ViewMatchers.withId(R.id.Recyclerview)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void filterByDate() {
        //Ajoute une première réunion
        onView(ViewMatchers.withId(R.id.add_meeting_button)).perform(click());
        onView(ViewMatchers.withId(R.id.meetingPlace)).perform(typeText("Test"));
        onView(ViewMatchers.withId(R.id.date_picker)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2020, 04, 7));
        onView(withId(android.R.id.button1)).perform(click());
        onView(ViewMatchers.withId(R.id.valider)).perform(click());
        //Ajoute une deuxième réunion
        onView(ViewMatchers.withId(R.id.add_meeting_button)).perform(click());
        onView(ViewMatchers.withId(R.id.meetingPlace)).perform(typeText("Salle B"));
        onView(ViewMatchers.withId(R.id.date_picker)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2020, 04, 8));
        onView(withId(android.R.id.button1)).perform(click());
        onView(ViewMatchers.withId(R.id.valider)).perform(click());
        //Filtre par date
        onView(ViewMatchers.withId(R.id.filtrer)).perform(click());
        onData(CoreMatchers.anything())
                .inRoot(RootMatchers.isPlatformPopup())
                .inAdapterView(CoreMatchers.<View>instanceOf(MenuPopupWindow.MenuDropDownListView.class))
                .atPosition(0)
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(2020, 04, 7));
        onView(withId(android.R.id.button1)).perform(click());
        onView(ViewMatchers.withId(R.id.Recyclerview)).check(matches(hasMinimumChildCount(1)));
    }

}
