package com.natevaughan.kchat.api

/**
 * Created by nate on 11/23/17
 *
 * ReST-specific entities
 */
class RestMessage(val text: String)
class CreateChat(val invitees: Array<String>)