package com.example.submisionintermediate.view.costumeView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.example.submisionintermediate.R

class PassCostume : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val errortext = "Password harus memiliki setidaknya 8 karakter"
                    val passError=rootView.findViewById<TextView>(R.id.errorpass)

                    if ((s?.length ?: 0) < 8) {
                        passError.text=errortext
                    }else {
                        passError.text=null
                    }

                }
            })
    }
}