package com.example.ffive.seg2105ffiveproject;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.constraint.ConstraintLayout;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class WelcomeHomeOwnerTest {
    @Rule

    public ActivityTestRule<WelcomeHomeOwner> mActivityTestRule = new ActivityTestRule<WelcomeHomeOwner>(WelcomeHomeOwner.class);
    private WelcomeHomeOwner mActivity = null;

    private Button button2;
    private EditText myText1;
    private TextView myText2;
    private ListView listView1;
    private Spinner spinner1;
    private RelativeLayout sample1;
    private LinearLayout sample2;
    private ConstraintLayout sample3;
    //Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SignUpActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception{
        mActivity = mActivityTestRule.getActivity();


    }

    @Test
 public void test1()
    {
        button2 = mActivity.findViewById(R.id.buttonSearch);
        assertNotNull(button2);
    }
    @Test
    public void test2(){
        button2 = mActivity.findViewById(R.id.buttonLogOutHome);
        assertNotNull(button2);
    }
@Test
    public void test3(){
        myText1 = mActivity.findViewById(R.id.editTextSearchBar);
    assertNotNull(myText1);
}
@Test
public void test4(){
        myText2 = mActivity.findViewById(R.id.welcomeHomeTextView);
    assertNotNull(myText2);
}
@Test
public void test5(){
        myText2 = mActivity.findViewById(R.id.textView12);
    assertNotNull(myText2);
}
@Test
public void test6(){
        listView1 = mActivity.findViewById(R.id.lvSearch);
    assertNotNull(listView1);
}
@Test
public void test7(){
        spinner1 = mActivity.findViewById(R.id.dropdownSearchBy);
    assertNotNull(spinner1);
}
@Test
public void test8(){
    sample1 = mActivity.findViewById(R.id.final1);
    assertNotNull(sample1);
}
@Test
public void test9(){
   sample2 = mActivity.findViewById(R.id.final2);
    assertNotNull(sample2);
}
@Test
public void test10(){
    sample3 = mActivity.findViewById(R.id.final3);
    assertNotNull(sample3);
}


    @After
    public void tearDown(){
        mActivity = null;

    }
}