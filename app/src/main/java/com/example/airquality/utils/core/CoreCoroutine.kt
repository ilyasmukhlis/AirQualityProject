package com.example.airquality.utils.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.airquality.domain.`object`.*
import com.example.airquality.utils.EventWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.net.ssl.HttpsURLConnection

/**
 * The CoreCoroutine Interface responsible for performing all coroutine scope operations
 */

interface CoreCoroutine {

    val scope: CoroutineScope

    /**
     * Error output when processing http requests
     */
    val errorEventLiveData: LiveData<EventWrapper<String>>

    /**
     * Status during http request
     */
    val statusEventLiveData: LiveData<EventWrapper<Status>>

    /**
     * Error output for a specific field
     */
    val errorEventByTypeLiveData: LiveData<EventWrapper<UIValidation>>


    /**
     * Error output with error code
     */
    val errorEventByCodeLiveData: LiveData<EventWrapper<Pair<Int, String>>>

    /**
     * Message output
     */
    val successMessageEventLiveData: LiveData<EventWrapper<String>>


    /**
     * Calling the software progress bar
     */
    val loadingByTypeEvent: LiveData<EventWrapper<Pair<String, Boolean>>>


    /**
     * starting coroutine
     * Allows you to accept your type in the error response
     * @param block suspend function
     * @param result result
     * @param errorBlock error block (by default String)
     * @param isPullToRefresh if the reboot occurred via swipe then pass true
     * * If you need to use your tiv in an error, use [launch With Error]
     */
    fun <T : Any> launch(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((String?) -> Unit?)? = null,
        errorWithCodeBlock: ((Int, String?) -> Unit?)? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        loadingType: LoadingType = LoadingType.DEFAULT,
        loading: ((Boolean) -> Unit?)? = null
    )


    /**
     *  Starting the cold flow (flow)
     * @*@param request Flow request as a suspend function
     * @param result result
     * @param errorBlock error block (by default String)
     * @param isPullToRefresh if the reboot occurred via swipe then pass true
     * For more information https://kotlinlang.org/docs/reference/coroutines/flow.html
     */
    fun <T> launchFlow(
        requestFlow: suspend () -> Flow<T>,
        result: (T) -> Unit,
        errorBlock: ((String?) -> Unit?)? = null,
        errorWithCodeBlock: ((Int, String?) -> Unit?)? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        loadingType: LoadingType = LoadingType.DEFAULT,
        loading: ((Boolean) -> Unit?)? = null
    )

    /**
     * Starting a cold flow (flow) by error handling
     * @*@param request Flow request as a suspend function
     * @param result result
     * @param errorBlock error block (by default String)
     * @param isPullToRefresh if the reboot occurred via swipe then pass true
     * For more information https://kotlinlang.org/docs/reference/coroutines/flow.html
     */
    fun <T : Any, V : Any> launchFlowWithError(
        requestFlow: suspend () -> Flow<T>,
        result: (T) -> Unit,
        errorBlock: ((V?) -> Unit?)? = null,
        errorWithCodeBlock: ((Int, V?) -> Unit?)? = null,
        errorPrinter: NetworkErrorHttpPrinter<V>? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        loadingType: LoadingType = LoadingType.DEFAULT,
        loading: ((Boolean) -> Unit?)? = null
    )

    /**
     * starting coroutine with the application of a user error from the backup
     * Allows you to accept your type in the error response
     * @param block suspend function
     * @param result result
     * @param errorBlock error block (we pass our type)
     * @param isPullToRefresh if the reboot occurred via swipe, then we pass true
     */
    fun <T : Any, V : Any> launchWithError(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((V?) -> Unit?)? = null,
        errorWithCodeBlock: ((Int, V?) -> Unit?)? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        loadingType: LoadingType = LoadingType.DEFAULT,
        loading: ((Boolean) -> Unit?)? = null
    )


    /**
     * The error handler for ViewModel is used to handle an error with a custom model
     * @param isPullToRefresh if the reboot occurred via swipe then pass true
     * @param result result
     * @param success Block getting result (returns type)
     * @param error Block getting error (Returns type passed to [launch With Error])
     */
    suspend fun <T : Any, V : Any> unwrapWithError(
        loadingType: LoadingType = LoadingType.DEFAULT,
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((V) -> Unit?)? = null,
        errorWithCodeBlock: ((Int, V) -> Unit?)? = null,
        loading: ((Boolean) -> Unit?)? = null
    )


    /**
     * starting coroutine with the application of a user error from the backup
     * Allows you to accept your type in the error response
     * @param isPullToRefresh if the reboot occurred via swipe then pass true
     * @param block suspend function
     * @param result result
     * @param errorBlock error block with String type
     */
    suspend fun <T : Any> unwrap(
        loadingType: LoadingType = LoadingType.DEFAULT,
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((String) -> Unit?)? = null,
        errorWithCodeBlock: ((Int, String) -> Unit?)? = null,
        loading: ((Boolean) -> Unit?)? = null
    )

    /**
     * output of an error message
     */
    fun showError(msg: String)

    /**
     * Output a message for a specific field
     * @param error Message the message that we show on the UI
     * @param type type for example Type.password (set in the current module for a specific field)
     */
    fun showErrorByType(errorMessage: String?, type: String?)


    /**
     * output a message with an error code
     * @param error Message the message that we show on the UI
     * @param code error code
     */
    fun showErrorByCode(errorMessage: String, code: Int)

    /**
     * Showing loader by type
     */
    fun showLoadingByType(type: String, isLoading: Boolean)

    /**
     * Clearing coroutine (if used in conjunction with CoreLaunchViewModel
     * or CoreAndroidLaunchViewModel then the cleanup happens automatically)
     */
    fun clearCoroutine()

    /**
     * message output
     */
    fun showSuccessMessage(msg: String)

}
/**
 * The CoreCoroutineDelegate class responsible for implementation of the methods in CoreCoroutine Interface
 */

class CoreCoroutineDelegate : CoreCoroutine {

    private val parentJob = Job()
    override val scope = CoroutineScope(Dispatchers.Main + parentJob)


    private val _statusEventLiveData = MutableLiveData<EventWrapper<Status>>()
    override val statusEventLiveData: LiveData<EventWrapper<Status>>
        get() = _statusEventLiveData


    private val _errorEventLiveData = MutableLiveData<EventWrapper<String>>()
    override val errorEventLiveData: LiveData<EventWrapper<String>>
        get() = _errorEventLiveData


    private val _errorEventByTypeLiveData = MutableLiveData<EventWrapper<UIValidation>>()
    override val errorEventByTypeLiveData: LiveData<EventWrapper<UIValidation>>
        get() = _errorEventByTypeLiveData

    private val _errorEventByCodeLiveData = MutableLiveData<EventWrapper<Pair<Int, String>>>()
    override val errorEventByCodeLiveData: LiveData<EventWrapper<Pair<Int, String>>>
        get() = _errorEventByCodeLiveData

    private val _successMessageEventLiveData = MutableLiveData<EventWrapper<String>>()
    override val successMessageEventLiveData: LiveData<EventWrapper<String>>
        get() = _successMessageEventLiveData

    private val _loadingByTypeEvent = MutableLiveData<EventWrapper<Pair<String, Boolean>>>()
    override val loadingByTypeEvent: LiveData<EventWrapper<Pair<String, Boolean>>>
        get() = _loadingByTypeEvent

    override fun <T : Any> launch(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((String?) -> Unit?)?,
        errorWithCodeBlock: ((Int, String?) -> Unit?)?,
        dispatcher: CoroutineDispatcher,
        loadingType: LoadingType,
        loading: ((Boolean) -> Unit?)?
    ) {
        scope.launch(dispatcher) {
            if (loading == null)
                showLoading(loadingType)
            else
                loading.invoke(true)
            val value = block()
            unwrap(loadingType, value, {
                result(it)
            }, errorBlock, errorWithCodeBlock, loading)
        }
    }


    override fun <T> launchFlow(
        requestFlow: suspend () -> Flow<T>,
        result: (T) -> Unit,
        errorBlock: ((String?) -> Unit?)?,
        errorWithCodeBlock: ((Int, String?) -> Unit?)?,
        dispatcher: CoroutineDispatcher,
        loadingType: LoadingType,
        loading: ((Boolean) -> Unit?)?
    ) {
        scope.launch {
            requestFlow.invoke()
                .onStart {
                    if (loading == null)
                        showLoading(loadingType)
                    else
                        loading.invoke(true)
                }
                .onCompletion {
                    if (loading == null)
                        hideLoadingError(loadingType)
                    else
                        loading.invoke(false)

                }
                .collect {
                    result(it)
                }
        }
    }

//    .handleTypeError<V>({ code, message ->
//
//
//    }, ErrorHttpResponse())

    override fun <T : Any, V : Any> launchFlowWithError(
        requestFlow: suspend () -> Flow<T>,
        result: (T) -> Unit,
        errorBlock: ((V?) -> Unit?)?,
        errorWithCodeBlock: ((Int, V?) -> Unit?)?,
        errorPrinter: NetworkErrorHttpPrinter<V>?,
        dispatcher: CoroutineDispatcher,
        loadingType: LoadingType,
        loading: ((Boolean) -> Unit?)?
    ) {
        scope.launch {
            requestFlow.invoke()
                .onStart {
                    if (loading == null)
                        showLoading(loadingType)
                    else
                        loading.invoke(true)
                }
                .onCompletion {
                    if (loading == null)
                        hideLoadingError(loadingType)
                    else
                        loading.invoke(false)

                }
                .collect {
                    result(it)
                }
        }
    }

    override fun <T : Any, V : Any> launchWithError(
        block: suspend () -> ResultApi<T>,
        result: (T?) -> Unit,
        errorBlock: ((V?) -> Unit?)?,
        errorWithCodeBlock: ((Int, V?) -> Unit?)?,
        dispatcher: CoroutineDispatcher,
        loadingType: LoadingType,
        loading: ((Boolean) -> Unit?)?
    ) {
        scope.launch(dispatcher) {
            if (loading == null)
                showLoading(loadingType)
            else
                loading.invoke(true)
            val value = block()
            unwrapWithError(loadingType, value, {
                result(it)
            }, errorBlock, errorWithCodeBlock, loading)
        }

    }


    override suspend fun <T : Any, V : Any> unwrapWithError(
        loadingType: LoadingType,
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((V) -> Unit?)?,
        errorWithCodeBlock: ((Int, V) -> Unit?)?,
        loading: ((Boolean) -> Unit?)?
    ) {
        when (result) {
            is ResultApi.Success -> {
                hideLoadingSuccess(loadingType)
                successBlock(result.data)
            }
            is ResultApi.HttpError<*> -> {
                val error = (result.error as? V) ?: return

                try {
                    when {
                        errorBlock != null -> errorBlock.invoke(error)
                        errorWithCodeBlock != null -> errorWithCodeBlock.invoke(result.code, error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (result.code == HttpsURLConnection.HTTP_UNAUTHORIZED /*&& !getPref().getAccessToken()
                        .isNullOrEmpty()*/
                ) {
                    withContext(Dispatchers.Main) {
                        _statusEventLiveData.value = EventWrapper(Status.REDIRECT_LOGIN)
                    }
                    return
                }

                if (loading == null)
                    hideLoadingSuccess(loadingType)
                else
                    loading.invoke(false)
            }
        }
    }

    override suspend fun <T : Any> unwrap(
        loadingType: LoadingType,
        result: ResultApi<T>,
        successBlock: (T?) -> Unit,
        errorBlock: ((String) -> Unit?)?,
        errorWithCodeBlock: ((Int, String) -> Unit?)?,
        loading: ((Boolean) -> Unit?)?
    ) {
        when (result) {
            is ResultApi.Success -> {

                if (loading == null)
                    hideLoadingSuccess(loadingType)
                else
                    loading.invoke(false)

                successBlock(result.data)
            }
            is ResultApi.HttpError<*> -> {

                val error = result.error as? String ?: ""

                when {
                    errorBlock != null -> errorBlock.invoke(error)
                    errorWithCodeBlock != null -> errorWithCodeBlock.invoke(result.code, error)
                    else -> {
                        _errorEventLiveData.value = EventWrapper(error)
                        _errorEventByCodeLiveData.value = EventWrapper(result.code to error)
                    }
                }

                if (result.code == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    withContext(Dispatchers.Main) {
                        _statusEventLiveData.value = EventWrapper(Status.REDIRECT_LOGIN)
                    }
                    return
                }

                if (loading == null)
                    hideLoadingSuccess(loadingType)
                else
                    loading.invoke(false)
            }
        }
    }

    override fun showError(msg: String) {
        _errorEventLiveData.value = EventWrapper(msg)
    }

    override fun showErrorByType(errorMessage: String?, type: String?) {
        _errorEventByTypeLiveData.value =
            EventWrapper(UIValidation(type.orEmpty(), errorMessage.orEmpty()))

    }

    override fun showLoadingByType(type: String, isLoading: Boolean) {
        _loadingByTypeEvent.value = EventWrapper(Pair(type, isLoading))
    }

    override fun showSuccessMessage(msg: String) {
        _successMessageEventLiveData.value = EventWrapper(msg)
    }

    override fun showErrorByCode(errorMessage: String, code: Int) {
        _errorEventByCodeLiveData.value = EventWrapper(code to errorMessage)
    }

    override fun clearCoroutine() {
        parentJob.cancelChildren()
    }


    /**
     * Launching live data to display the loader
     * @param loading Type loader type
     */
    private suspend fun showLoading(loadingType: LoadingType) {
        withContext(Dispatchers.Main) {
            _statusEventLiveData.value = when (loadingType) {
                LoadingType.DEFAULT -> {
                    EventWrapper(Status.SHOW_LOADING)
                }

                LoadingType.PAGGING -> {
                    EventWrapper(Status.SHOW_PAGGING_LOADING)
                }
                LoadingType.PULL_TO_REFRESH -> {
                    EventWrapper(Status.SHOW_PULL_TO_REFRESH_LOADING)
                }
                LoadingType.NONE -> {
                    return@withContext
                }
            }
        }
    }

    /**
     * Hiding the loader with a successful request
     * @param loading Type loader type
     */
    private suspend fun hideLoadingSuccess(loadingType: LoadingType) {
        withContext(Dispatchers.Main) {
            _statusEventLiveData.value = when (loadingType) {
                LoadingType.DEFAULT -> {
                    EventWrapper(Status.HIDE_LOADING)
                }

                LoadingType.PAGGING -> {
                    EventWrapper(Status.HIDE_PAGGING_LOADING)
                }
                LoadingType.PULL_TO_REFRESH -> {
                    EventWrapper(Status.HIDE_PULL_TO_REFRESH_LOADING)
                }
                LoadingType.NONE -> {
                    return@withContext
                }
            }
        }
    }

    /**
     * Hiding the loader in case of a request error
     * @param loading Type loader type
     */
    private suspend fun hideLoadingError(loadingType: LoadingType) {
        withContext(Dispatchers.Main) {
            _statusEventLiveData.value = when (loadingType) {
                LoadingType.DEFAULT -> {
                    EventWrapper(Status.HIDE_LOADING)
                }
                LoadingType.PAGGING -> {
                    EventWrapper(Status.HIDE_PAGGING_LOADING)
                }
                LoadingType.PULL_TO_REFRESH -> {
                    EventWrapper(Status.HIDE_PULL_TO_REFRESH_LOADING)
                }
                LoadingType.NONE -> {
                    return@withContext
                }
            }
            _statusEventLiveData.value = EventWrapper(Status.ERROR)
        }
    }
}