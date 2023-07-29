package com.example.airquality.utils.core

/**
 * The CoreLaunchUseCase is a skeleton of other usecases which require sending parameters inside Coroutine Scope
 */

abstract class CoreLaunchUseCase<in T, out V> : CoreCoroutine by CoreCoroutineDelegate() {
    abstract fun execute(param: T, result: ((V) -> Unit))
}

/**
 * The CoreNonParamLaunchUseCase is a skeleton of other usecases which does not require sending parameters inside Coroutine Scope
 */

abstract class CoreNonParamLaunchUseCase<out V> : CoreCoroutine by CoreCoroutineDelegate() {
    abstract fun execute(result: ((V) -> Unit))
}
