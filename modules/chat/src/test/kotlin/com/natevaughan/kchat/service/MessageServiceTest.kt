package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.framework.NotFoundException
import com.natevaughan.kchat.service.DomainFactory.buildValidMessage
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

/**
 * Created by nate on 12/10/17
 */
class MessageServiceTest {

    @Test
    fun itShouldCallDeleteOnRepoIfUserPassesValidation() {
        val id = 1L

        val message = buildValidMessage()
        val user = message.author

        val messageRepo = mock(MessageRepo::class.java)
        val hatService = mock(ChatService::class.java)
        val messageService = MessageService(messageRepo, hatService)

        `when`(messageRepo.findById(id)).thenReturn(message)

        messageService.delete(id, user)

        verify(messageRepo).delete(message)
    }

    @Test(expected = NotFoundException::class)
    fun itShouldThrowAnExceptionIfUserIsNotAuthor() {
        val id = 1L

        val message = buildValidMessage()
        val user = message.author

        val messageRepo = mock(MessageRepo::class.java)
        val hatService = mock(ChatService::class.java)
        val messageService = MessageService(messageRepo, hatService)

        messageService.delete(id, user)
        verify(messageRepo).findById(id)
        verifyNoMoreInteractions(messageRepo)
    }
}