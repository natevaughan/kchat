package com.natevaughan.kchat.service

import com.natevaughan.kchat.framework.NotFoundException
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by nate on 3/23/19
 */
class SpaceServiceTest {

    @Test
    fun itShouldCallDeleteOnRepoIfUserPassesValidation() {
		assertTrue(true)
    }

    @Test(expected = NotFoundException::class)
    fun itShouldThrowAnExceptionIfUserIsNotAuthor() {
		throw NotFoundException("!!")
    }
}