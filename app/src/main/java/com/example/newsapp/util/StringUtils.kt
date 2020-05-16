package com.example.newsapp.util;

class StringUtils {

    companion object {

        fun checkValidString(string: String?): Boolean {
            string?.let {
                return it.isNotBlank() && it.isNotEmpty()
            } ?: run {
                return false
            }
        }

        fun checkValidNumber(string: String?): Boolean {
            string?.let {
                return it.isNotBlank() && it.isNotEmpty() && it.length > 6 && it.length < 15
            } ?: run {
                return false
            }
        }

        fun checkValidOtp(string: String?): Boolean {
            string?.let {
                return it.isNotBlank() && it.isNotEmpty() && it.length == 6
            } ?: run {
                return false
            }
        }
    }
}
