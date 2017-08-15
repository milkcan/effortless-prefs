package io.milkcan.effortlessprefs.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.pixplicity.easyprefs.sample.R
import io.milkcan.effortlessandroid.toast
import io.milkcan.effortlessprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SAVED_TEXT: String = "saved_text"
        private const val SAVED_NUMBER: String = "saved_number"
        private const val FROM_INSTANCE_STATE: String = " : from instance state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // get the saved String from the preference by key, and give a default value
        // if Prefs does not contain the key.
        val s = Prefs.getString(SAVED_TEXT, getString(R.string.not_found))
        val d = Prefs.getDouble(SAVED_NUMBER, -1.0)
        updateText(s)
        updateNumber(d, false)

        bt_save_text.setOnClickListener {
            val text = et_text?.text.toString()
            if (!TextUtils.isEmpty(text)) {
                // one liner to save the String.
                Prefs.putString(SAVED_TEXT, text)
                updateText(text)
            } else {
                toast("Trying to save a text with length 0")
            }
        }

        bt_save_number.setOnClickListener {
            val d = java.lang.Double.parseDouble(et_number?.text.toString())
            Prefs.putDouble(SAVED_NUMBER, d)
            updateNumber(d, false)
        }

        bt_force_close.setOnClickListener { finish() }
    }

    private fun updateText(s: String?) {
        val text = String.format(getString(R.string.saved_text), s)
        tv_saved_text?.text = text
    }

    private fun updateNumber(d: Double, fromInstanceState: Boolean) {
        val text = String.format(getString(R.string.saved_number), d.toString(), if (fromInstanceState) FROM_INSTANCE_STATE else "")
        tv_saved_number?.text = text
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val text = et_text?.text.toString()
        if (!TextUtils.isEmpty(text)) {
            outState.putString(SAVED_TEXT, text)
        }
        val d = java.lang.Double.parseDouble(et_number?.text.toString())
        outState.putDouble(SAVED_NUMBER, d)
    }

    override fun onRestoreInstanceState(state: Bundle) {
        super.onRestoreInstanceState(state)
        if (state.containsKey(SAVED_TEXT)) {
            val text = state.getString(SAVED_TEXT)!! + FROM_INSTANCE_STATE
            updateText(text)
        }

        if (state.containsKey(SAVED_NUMBER)) {
            val d = state.getDouble(SAVED_NUMBER)
            updateNumber(d, true)
        }
    }

}