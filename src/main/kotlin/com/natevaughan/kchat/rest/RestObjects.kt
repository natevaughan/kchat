package com.natevaughan.kchat.rest

import com.natevaughan.kchat.api.Chat

/**
 * Created by nate on 3/21/19
 */
data class TokenRedemptionRequest(val token: String, val alias: String?)
data class CreateInviteRequest(val spaceId: String, val memo: String)
data class CreateUserRequest(val name:String)
data class ApiKeyResponse(val userName: String, val apiKey: String)
data class RestChat(val name: String, val type: Chat.Type, val participants: Collection<String>?)
data class RestSpace(val name: String)
data class AddUserRequest(val userId: String)
data class MessageRequest(val text: String)