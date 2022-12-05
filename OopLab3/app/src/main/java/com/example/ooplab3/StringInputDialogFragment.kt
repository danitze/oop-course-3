package com.example.ooplab3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class StringInputDialogFragment : DialogFragment(R.layout.dialog_fragment_input_string) {

    var closeCallback: ((String) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button_ok).setOnClickListener {
            closeCallback?.invoke(view.findViewById<EditText>(R.id.edit_text_input_string).text.toString())
            dismiss()
        }
    }

}