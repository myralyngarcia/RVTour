package com.myralyn.rvtour

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myralyn.rvtour.utils.RecyclerViewMatcher
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationDrawerTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     * We need the following dependencies in the module gradle
     * androidTestImplementation 'androidx.test.ext:junit-ktx:1.1.3' -> this is in mvnrepository AndroidX Test Library
     */
    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun cityListFragmentTest() {

        navigateToMenuViaNavigationDrawer()

        /**
         * After clicking on the navigation drawer the layout for menu are displayed
         * Here we click on the menu 'Home' which has id, R.id.fragmentCityList in activity_drawer_items.xml
         * since we want to navigate to City List fragment
         */
        onView(withId(R.id.fragmentCityList)).perform(click())

        /**
         * Once we are in City List fragment we want to confirm the toolbar text is updated to 'Top Cities'
         */
        onView(withId(R.id.activity_main_toolbar)).check(matches(hasDescendant(withText("Top Cities"))))

        /**
         * Also we know that the second city displayed in recyclerView is 'Manchester' we want to confirm it as below
         */
        onView(RecyclerViewMatcher(R.id.city_recycler_view).atPosition(1)).check(
            matches(
                hasDescendant(withText("Manchester"))
            )
        )

        /**
         * also check each item/view object in recycler has image, name of city, icon heart and delete
         */
        onView(RecyclerViewMatcher(R.id.city_recycler_view).atPosition(0))
            .check(matches(hasDescendant(withId(R.id.imv_city))))
            .check(matches(hasDescendant(withId(R.id.txv_city_name))))
            .check(matches(hasDescendant(withId(R.id.imv_favorite))))
            .check(matches(hasDescendant(withId(R.id.imv_delete))))
    }

    @Test
    fun favouriteCityListFragmentTest(){

        /**
         * navigate to the menu
         */
        navigateToMenuViaNavigationDrawer()

        /**
         * from the menu click on favourite
         */
        onView(withId(R.id.fragmentFavoriteList)).perform(click())

        /**
         * Once we are in Favourite City List fragment we want to confirm the toolbar text is updated to 'Favourite'
         */
        onView(withId(R.id.activity_main_toolbar)).check(matches(hasDescendant(withText("Favorites"))))
    }

    /**
     * also test that we can mark an item in the recycler view as favourite and they are added in favourite fragment
     */
    @Test
    fun markCityFavouriteTest(){

        navigateToMenuViaNavigationDrawer()
        /**
         * Click on the menu home to see the City List
         */
        onView(withId(R.id.fragmentCityList)).perform(click())

        /**
         * mark 'Manchester' city as favourite which is the second item in the list
         */
        onView(RecyclerViewMatcher(R.id.city_recycler_view).atPositionOnView(1, R.id.imv_favorite)).perform(
            click())
        /**
         * navigate to the Favourite fragment by first clicking on the navigation drawer then click on Favorite menu
         */
        navigateToMenuViaNavigationDrawer()

        onView(withId(R.id.fragmentFavoriteList)).perform(click())

        onView(RecyclerViewMatcher(R.id.fav_city_recycler_view).atPositionOnView(0,R.id.txv_fav_city_name))
            .check(matches(withText("Manchester")))
    }

    /**
     * if an item marked as favourite and they are deleted from city list fragment confirm that they are also deleted in favourite fragment
     */
    @Test
    fun markCityFavoriteThenDeleteCityTest(){
        navigateToMenuViaNavigationDrawer()
        /**
         * Click on the menu home to see the City List
         */
        onView(withId(R.id.fragmentCityList)).perform(click())

        /**
         * Mark 'New Delhi' as favourite too, 1st from list
         */
        onView(RecyclerViewMatcher(R.id.city_recycler_view).atPositionOnView(0, R.id.imv_favorite)).perform(
            click())


        /**
         * Mark 'Nottingham' as favourite too, 3rd from list
         * first we need to scroll to 3rd item since its not completely displayed
         */
        onView(withId(R.id.city_recycler_view)).perform(scrollToPosition<RecyclerView.ViewHolder>(2))
        onView(RecyclerViewMatcher(R.id.city_recycler_view).atPositionOnView(2, R.id.imv_favorite)).perform(
            click())

        /**
         * navigate to the Favourite fragment by first clicking on the navigation drawer then click on Favorite menu
         */
        navigateToMenuViaNavigationDrawer()

        onView(withId(R.id.fragmentFavoriteList)).perform(click())

        /**
         * confirm New Delhi is 2nd item in favourite city fragment
         */
        onView(RecyclerViewMatcher(R.id.fav_city_recycler_view).atPositionOnView(1,R.id.txv_fav_city_name))
            .check(matches(withText("New Delhi")))
        /**
         * navigate back to city list by clicking the back button on device
         */
        pressBack()

        /**
         * and delete city 'New Delhi'
         * We know city New Delhi is the first item (position: 0) in the City List so we click delete
         */
        onView(RecyclerViewMatcher(R.id.city_recycler_view).atPositionOnView(0, R.id.imv_delete))
            .perform(click())
        //confirm New Delhi is deleted from the favourite fragment
        navigateToMenuViaNavigationDrawer()
        onView(withId(R.id.fragmentFavoriteList)).perform(click())
        onView(RecyclerViewMatcher(R.id.fav_city_recycler_view).atPositionOnView(1, R.id.txv_fav_city_name))
            .check(matches(not(withText("New Delhi"))))
    }

    private fun navigateToMenuViaNavigationDrawer(){
        /**
         * click on the drawer layout from MainActivity to access the toolbar where navigation drawer is in
         */
        onView(withId(R.id.drawer_layout)).perform(click())

        /**
         * Note: when using Navigation Component design the navigation drawer is added in materialToolbar automatically
         * Hence parent of navigation drawer is our materialToolbar that has id, R.id.activity_main_toolbar
         * We find the navigation drawer view via the parent toolbar then we click on the view
         */
        onView(
            allOf(
                withParent(withId(R.id.activity_main_toolbar)),
                withContentDescription("Open navigation drawer")
            )
        ).perform(click())
    }
}