package com.example.todolist.databeans.task

import androidx.recyclerview.widget.RecyclerView

/*  让参数延迟传入
class Person(
    val firstName: String,
    val lastName: String,
    val ageDelegate: DelegatedValue<Int>,
    val addressDelegate: DelegatedValue<String?>
) {

    var age by delayedValue(ageDelegate)
    var address by delayedValue(addressDelegate)

    constructor(firstName: String, lastName: String) : this(
        firstName,
        lastName,
        object : DelegatedValue<Int> {
            private var value: Int? = null
            override fun getValue(): Int = value ?: throw IllegalStateException("Age has not been initialized.")
            override fun setValue(value: Int) { this.value = value }
        },
        object : DelegatedValue<String?> {
            private var value: String? = null
            override fun getValue(): String? = value
            override fun setValue(value: String?) { this.value = value }
        })

}
*/
sealed class FoldData