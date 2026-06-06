package com.migvidal.wikicircuit.core.exception

class NullResponseException(val target: String): RuntimeException("$target was null")