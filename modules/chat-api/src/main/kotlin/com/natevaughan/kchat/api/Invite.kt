package com.natevaughan.kchat.api

import com.fasterxml.jackson.annotation.JsonIgnore

import java.time.Instant

/**
 * Created by nate on 3/20/19
 */
class Invite(val token: String, @get:JsonIgnore
val dateCreated: Instant, val space: Space, @get:JsonIgnore val sender: User, val memo: String, val expires: Instant, val recipient: User?, val dateRedeemed: Instant?)
