package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.NotFoundException
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import java.time.Instant
import java.util.UUID

/**
 * Created by nate on 12/10/17
 */
class MessageServiceTest {

    @Test
    fun itShouldCallDeleteOnRepoIfUserPassesValidation() {
		val message = buildValidMessage()

		val id = message.id
        val user = message.author

        val messageRepo = mock(MessageRepo::class.java)
        val chatService = mock(ChatService::class.java)
        val messageService = DefaultMessageService(messageRepo, chatService)

        `when`(messageRepo.findById(id)).thenReturn(message)

        messageService.delete(id, user)

        verify(messageRepo).delete(message)
    }

    @Test(expected = NotFoundException::class)
    fun itShouldThrowAnExceptionIfUserIsNotAuthor() {

        val message = buildValidMessage()
        val id = message.id
		val requester = User(UUID.randomUUID().toString())

        val messageRepo = mock(MessageRepo::class.java)
		`when`(messageRepo.findById(id)).thenReturn(message)
        val chatService = mock(ChatService::class.java)
        val messageService = DefaultMessageService(messageRepo, chatService)

		messageService.delete(id, requester)
        verify(messageRepo).save(message)
		verifyNoMoreInteractions(messageRepo)
    }

	private fun buildValidMessage(): Message {
		return Message(
				UUID.randomUUID().toString(),
				Instant.now(),
				"hello, world",
				User(
						UUID.randomUUID().toString()
				),
				Chat(UUID.randomUUID().toString()),
				Instant.now()
		)
	}
}