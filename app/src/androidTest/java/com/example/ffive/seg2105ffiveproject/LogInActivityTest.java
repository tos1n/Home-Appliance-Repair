package com.example.ffive.seg2105ffiveproject;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.EditText;
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

public class LogInActivityTest {
    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<LogInActivity>(LogInActivity.class);
    private LogInActivity mActivity = null;
    private Button butto;
    private Button button2;
    private EditText myText1;
    private EditText myText2;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SignUpActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception{
        mActivity = mActivityTestRule.getActivity();

    }

//    @Test
//    public void rightNow()
//    {
//        butto = mActivity2.findViewById(R.id.buttonSearch);
//        assertNotNull(butto);
//    }
    @Test

    public void testLauch(){
        butto = mActivity.findViewById(R.id.buttonLogIn);
        assertNotNull(butto);
        System.out.println("Heeeey" + mActivity);
    }
    @Test
    public void testLauch2(){
        button2 = mActivity.findViewById(R.id.buttonSignUp);
        assertNotNull(button2);
    }
    @Test
    public void testUsername(){
        ServiceProvider testServiceProvider = new ServiceProvider("omar","mohsen");
        assertEquals("make sure you input the right user", "omar",testServiceProvider.getUsername());
    }
    @Test
    public void testUsername2(){
        HomeOwner testHomeOwner = new HomeOwner("omar","mohsen");
        assertEquals("make sure you select the right user", "omar",testHomeOwner.getUsername());
    }
    @Test
    public void testUsername3(){ // deliverable 3
        ServiceProvider testHomeOwner2 = new ServiceProvider("omar","mohsen");
        assertEquals("make sure you select the right service", false, testHomeOwner2.offersService("plumber"));
    }
    @Test
    public void testUsername4(){ // deliverable 3
        ServiceProvider testHomeOwner5 = new ServiceProvider("omar","mohsen");
        assertEquals("make sure you delete properly", false, testHomeOwner5.deleteService("plumber"));
    }
    @Test
    public void testSignupchecker(){
        assertNotNull(mActivity.findViewById(R.id.buttonSignUp));
        onView(withId(R.id.buttonSignUp)).perform(click());
        Activity signUpActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNotNull(signUpActivity);
        signUpActivity.finish();
    }
    //deliverable 3 unit tests
    @Test
    public void test6(){
        myText1 = mActivity.findViewById(R.id.editTextUsername);
        assertNotNull(myText1);
    }

    @Test
    public void test7(){
        myText2 = mActivity.findViewById(R.id.editTextPassword);
        assertNotNull(myText2);
    }
    @Test
    public void test8(){
        ServiceProvider testHomeOwner9 = new ServiceProvider("omar","mohsen");
        assertEquals("make sure you select the right day", "Not Available", testHomeOwner9.dayAvailability("Monday"));
    }
    @Test
    public void test9(){
        ServiceProvider testHomeOwner9 = new ServiceProvider("omar","mohsen");
        assertEquals("make sure you put in the right time", "9:30 to 17:30", testHomeOwner9.setAvailability("Monday", "9:30", "17:30"));
    }
    @After
    public void tearDown(){
        mActivity = null;
    }
}