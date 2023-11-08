package com.example.demo.helper

/**
 *  This [StateEventWithContent] can have exactly 2 states like the [StateEvent] but the triggered state holds a value of type [T].
 */
sealed interface StateEventWithContent<T> {

    data class StateEventWithContentTriggered<T>(val content: T) : StateEventWithContent<T>

    class StateEventWithContentConsumed<T> : StateEventWithContent<T>
}

/**
 * A shorter and more readable way to create an [StateEventWithContent] in its triggered state holding a value of [T].
 * @param content A value that is needed on the event consumer side.
 */
fun <T> triggered(content: T) = StateEventWithContent.StateEventWithContentTriggered(content)

/**
 * A shorter and more readable way to create an [StateEventWithContent] in its consumed state.
 */
fun <T> consumed() = StateEventWithContent.StateEventWithContentConsumed<T>()