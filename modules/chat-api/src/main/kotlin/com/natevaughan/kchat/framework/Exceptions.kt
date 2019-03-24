package com.natevaughan.kchat.framework

///**
// * Created by nate on 3/21/19
// */
class BadRequestException(message: String): Exception(message)
class NotFoundException(message: String): Exception(message)
class UnauthorizedException(message: String): Exception(message)