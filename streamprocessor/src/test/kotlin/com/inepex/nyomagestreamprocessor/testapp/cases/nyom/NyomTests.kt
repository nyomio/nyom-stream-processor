package com.inepex.nyomagestreamprocessor.testapp.cases.nyom

import com.google.inject.Inject
import com.google.inject.Provider
import com.inepex.nyomagestreamprocessor.testapp.SkeletonService
import com.inepex.nyomagestreamprocessor.testapp.common.GuiceJUnitRunner
import com.inepex.nyomagestreamprocessor.testapp.common.TestAppGuiceModule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(GuiceJUnitRunner::class)
@GuiceJUnitRunner.GuiceModules(TestAppGuiceModule::class)
class NyomTests @Inject constructor(
        private val skeletonServiceProvider: Provider<SkeletonService>
) {

    @Test
    fun case1() {
        skeletonServiceProvider.get().execute(`Case 01 - Locations, shouldn't be grouped`())
    }

    @Test
    fun case2() {
        skeletonServiceProvider.get().execute(`Case 02 - Location with events and statuses, one group`())
    }

    @Test
    fun case3() {
        skeletonServiceProvider.get().execute(`Case 03 - Late location, should be grouped using an update`())
    }

    @Test
    fun case4() {
        skeletonServiceProvider.get().execute(`Case 04 - Events and statuses, should use latest nyom`())
    }

    @Test
    fun case5() {
        skeletonServiceProvider.get().execute(`Case 05 - Event with interpolated location`())
    }

}
