package com.natevaughan.kchat.framework

import javax.ws.rs.core.Response

/**
 * Created by nate on 12/3/17
 */
// 2xx
val OK = Response.status(Response.Status.OK).build()
val CREATED = Response.status(Response.Status.CREATED).build()

// 4xx
val BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build()
val UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build()
val NOT_FOUND = Response.status(Response.Status.NOT_FOUND).build()

// 5xx
val INTERNAL_SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
