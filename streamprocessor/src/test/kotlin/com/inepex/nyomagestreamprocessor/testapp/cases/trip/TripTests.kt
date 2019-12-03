package com.inepex.nyomagestreamprocessor.testapp.cases.trip

import com.google.inject.Inject
import com.google.inject.Provider
import com.inepex.nyomagestreamprocessor.testapp.SkeletonService
import com.inepex.nyomagestreamprocessor.testapp.cases.Case
import com.inepex.nyomagestreamprocessor.testapp.common.GuiceJUnitRunner
import com.inepex.nyomagestreamprocessor.testapp.common.TestAppGuiceModule
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(GuiceJUnitRunner::class)
@GuiceJUnitRunner.GuiceModules(TestAppGuiceModule::class)
class TripTests @Inject constructor(
        private val skeletonServiceProvider: Provider<SkeletonService>
) {

    @Test
    fun case1() {
        skeletonServiceProvider.get().execute(`Case 01 - Trip with waiting`())
    }

    @Test
    fun case2() {
        skeletonServiceProvider.get().execute(`Case 02 - Trip with valid jump`())
    }

    @Test
    fun case3() {
        skeletonServiceProvider.get().execute(`Case 03 - Trip with invalid positions`())
    }

    @Test
    fun case4() {
        skeletonServiceProvider.get().execute(`Case 04 - 20 minute real trip, only reports`())
    }
}
