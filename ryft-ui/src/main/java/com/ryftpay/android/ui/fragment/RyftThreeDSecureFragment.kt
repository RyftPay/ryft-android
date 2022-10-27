package com.ryftpay.android.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.ryftpay.android.ui.delegate.DefaultRyftThreeDSecureDelegate
import com.ryftpay.android.ui.delegate.RyftThreeDSecureDelegate
import com.ryftpay.android.ui.extension.getParcelableArgs
import com.ryftpay.android.ui.listener.RyftThreeDSecureListener
import com.ryftpay.ui.R
import kotlinx.parcelize.Parcelize
import java.lang.IllegalArgumentException

internal class RyftThreeDSecureFragment :
    DialogFragment(),
    RyftThreeDSecureListener {

    private lateinit var delegate: RyftThreeDSecureDelegate
    private lateinit var input: Arguments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        input = arguments?.getParcelableArgs(
            ARGUMENTS_BUNDLE_KEY,
            Arguments::class.java
        ) ?: throw IllegalArgumentException("No arguments provided to fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ryft_three_d_secure, container, false)
    }

    override fun onViewCreated(
        root: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(root, savedInstanceState)
        delegate = DefaultRyftThreeDSecureDelegate(listener = this)
        delegate.onViewCreated(root, input.redirectUri, input.returnUri)
    }

    override fun getTheme() = R.style.RyftFullscreenDialog

    override fun onThreeDSecureCompleted(paymentSessionId: String) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RESULT_BUNDLE_KEY,
            Result(paymentSessionId)
        )
        findNavController().popBackStack()
    }

    @Parcelize
    internal class Arguments(
        val redirectUri: Uri,
        val returnUri: Uri
    ) : Parcelable {
        companion object {
            fun bundleFrom(
                redirectUri: Uri,
                returnUri: Uri
            ): Bundle =
                Bundle().apply {
                    putParcelable(
                        ARGUMENTS_BUNDLE_KEY,
                        Arguments(redirectUri, returnUri)
                    )
                }
        }
    }

    @Parcelize
    internal class Result(
        val paymentSessionId: String
    ) : Parcelable

    companion object {
        internal const val RESULT_BUNDLE_KEY = "Result"
        private const val ARGUMENTS_BUNDLE_KEY = "Arguments"
    }
}
