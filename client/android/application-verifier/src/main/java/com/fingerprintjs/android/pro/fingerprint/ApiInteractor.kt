package com.fingerprintjs.android.pro.fingerprint


import com.fingerprintjs.android.pro.fingerprint.logger.Logger
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequest
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenRequestResult
import com.fingerprintjs.android.pro.fingerprint.requests.FetchTokenResponse
import com.fingerprintjs.android.pro.fingerprint.signals.Signal
import com.fingerprintjs.android.pro.fingerprint.transport.HttpClient
import com.fingerprintjs.android.pro.fingerprint.transport.RequestResultType
import com.fingerprintjs.android.pro.fingerprint.transport.ssl.SSLConnectionInspector


interface ApiInteractor {
    fun getToken(
        signals: List<Signal>
    ): FetchTokenResponse
}

class ApiInteractorImpl(
    private val httpClient: HttpClient,
    private val endpointURL: String,
    private val appId: String,
    private val logger: Logger,
    private val sslConnectionInspector: SSLConnectionInspector
) : ApiInteractor {
    override fun getToken(
        signals: List<Signal>
    ): FetchTokenResponse {

        if (!sslConnectionInspector.inspectConnection(endpointURL)) {
            return FetchTokenRequestResult(RequestResultType.ERROR, null).result()
        }
        
        val fetchTokenRequest = FetchTokenRequest(
            endpointURL, appId, signals
        )

        val requestResult = httpClient.performRequest(
            fetchTokenRequest
        )
        val response = FetchTokenRequestResult(requestResult.type, requestResult.rawResponse)
        requestResult.rawResponse?.let {
            logger.debug(this, String(it, Charsets.UTF_8))
        }
        return response.result()
    }
}