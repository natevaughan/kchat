package com.natevaughan.kchat.model.message

import com.natevaughan.hat.message.CacheMessageRepo
import com.natevaughan.kchat.util.DomainFactory.buildValidMessage
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by nate on 11/24/17
 */

class CacheMessageRepoTest {

    var repo = CacheMessageRepo()

    @Before
    fun setup() {
        repo = CacheMessageRepo()
    }

    @Test
    fun testSave() {
        val message = buildValidMessage()
        assertNull("message.id should be null before update", message.id)
        repo.save(message)
        assertNotNull("MessageRepo update should add an ID", message.id)
    }

    @Test
    fun testFindById() {
        val message = buildValidMessage()
        repo.save(message)
        val found = repo.findById(message.id!!)
        assertEquals("Message returned should be the same one saved", message, found)
    }

    @Test
    fun testFindSinceTimestamp() {
        val oldest = buildValidMessage().copy(timestamp = 1L)
        repo.save(oldest)
        for (i in 2..10) {
            repo.save(oldest.copy(text = oldest.text + i.toString(), timestamp = i.toLong()))
        }
        val recent = repo.findAllSinceTimestamp(3L)
        assertEquals("There should be 7 items", recent.count(), 7)

    }


    @Test
    fun testFindRecent() {
        val oldest = buildValidMessage().copy(timestamp = 1L)
        repo.save(oldest)
        for (i in 2..4) {
            repo.save(oldest.copy(text = oldest.text + i.toString(), timestamp = i.toLong()))
        }

        val newest = oldest.copy(text = oldest.text + 5, timestamp = 5L)
        repo.save(newest)
        assertNotNull(repo.findById(newest.id!!))
        val recent = repo.findRecent(3)
        assertEquals("There should be 3 items", recent.count(), 3)
        assertFalse("The oldest should be left off", recent.contains(oldest))
        assertTrue("The newest should be included", recent.contains(newest))
    }

    @Test
    fun testFindRecentTakeAll() {
        val count = 3
        for (i in 1..count) {
            repo.save(buildValidMessage().copy(text = "msg" + i.toString(), timestamp = i.toLong()))
        }

        val recent = repo.findRecent(count + 10)
        assertEquals("findRecent should only give the most recent $count", count, recent.count())
    }
}