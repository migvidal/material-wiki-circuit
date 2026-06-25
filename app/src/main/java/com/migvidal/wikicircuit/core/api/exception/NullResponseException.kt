package com.migvidal.wikicircuit.core.api.exception

class NullResponseException(val target: String): RuntimeException("$target was null")