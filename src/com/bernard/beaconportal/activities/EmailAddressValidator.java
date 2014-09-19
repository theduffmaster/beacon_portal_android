package com.bernard.beaconportal.activities;

import android.text.util.Rfc822Tokenizer;
import android.widget.AutoCompleteTextView.Validator;

public class EmailAddressValidator implements Validator {
	@Override
	public CharSequence fixText(CharSequence invalidText) {
		return "";
	}

	@Override
	public boolean isValid(CharSequence text) {
		return Rfc822Tokenizer.tokenize(text).length > 0;
	}

	public boolean isValidAddressOnly(CharSequence text) {
		return com.bernard.beaconportal.activities.helper.Regex.EMAIL_ADDRESS_PATTERN
				.matcher(text).matches();
	}
}
