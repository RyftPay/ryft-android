package com.ryftpay.android.core.model.error

import com.ryftpay.android.core.TestData.ryftErrorElementResponse
import com.ryftpay.android.core.TestData.ryftErrorResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

internal class RyftErrorTest {

    @Test
    fun `from should return request id when present`() {
        RyftError.from(ryftErrorResponse.copy(requestId = "test")).requestId shouldBeEqualTo "test"
    }

    @Test
    fun `from should not return request id when null`() {
        RyftError.from(ryftErrorResponse.copy(requestId = null)).requestId shouldBeEqualTo null
    }

    @Test
    fun `from should return http status code`() {
        RyftError.from(ryftErrorResponse).httpStatusCode shouldBeEqualTo ryftErrorResponse.code
    }

    @Test
    fun `from should return no errors when there are none`() {
        RyftError.from(ryftErrorResponse.copy(errors = listOf())).errors shouldBeEqualTo listOf()
    }

    @Test
    fun `from should return errors when there are some`() {
        RyftError.from(
            ryftErrorResponse.copy(
                errors = listOf(
                    ryftErrorElementResponse,
                    ryftErrorElementResponse
                )
            )
        ).errors shouldBeEqualTo listOf(
            RyftErrorElement.from(ryftErrorElementResponse),
            RyftErrorElement.from(ryftErrorElementResponse)
        )
    }
}
