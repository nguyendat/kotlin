// !DIAGNOSTICS: -UNUSED_PARAMETER
// SKIP_TXT

import kotlin.coroutines.experimental.CoroutineContext

fun ordinal() {
    kotlin.coroutines.experimental.intrinsics.<!DEPRECATION, ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
    kotlin.coroutines.experimental.<!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
}

suspend fun named() {
    kotlin.coroutines.experimental.intrinsics.<!DEPRECATION!>coroutineContext<!>
    kotlin.coroutines.experimental.coroutineContext
}

class A {
    val coroutineContextOld = kotlin.coroutines.experimental.intrinsics.<!DEPRECATION, ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
    val coroutineContextNew = kotlin.coroutines.experimental.<!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
}

class Controller {
    fun ordinal() {
        kotlin.coroutines.experimental.intrinsics.<!DEPRECATION, ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
        kotlin.coroutines.experimental.<!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!>
    }

    suspend fun named() {
        kotlin.coroutines.experimental.intrinsics.<!DEPRECATION!>coroutineContext<!>
        kotlin.coroutines.experimental.coroutineContext
    }

    suspend fun severalArgs(s: String, a: Any) {
        kotlin.coroutines.experimental.intrinsics.<!DEPRECATION!>coroutineContext<!>
        kotlin.coroutines.experimental.coroutineContext
    }
}

fun builder(c: () -> CoroutineContext) = {}
fun builderSuspend(c: suspend () -> CoroutineContext) = {}

fun builderSeveralArgs(c: (Int, Int, Int) -> CoroutineContext) = {}
fun builderSuspendSeveralArgs(c: suspend (Int, Int, Int) -> CoroutineContext) = {}

fun test() {
    builder { kotlin.coroutines.experimental.intrinsics.<!DEPRECATION, ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!> }
    builder { kotlin.coroutines.experimental.<!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!> }
    builderSuspend { kotlin.coroutines.experimental.intrinsics.<!DEPRECATION!>coroutineContext<!> }
    builderSuspend { kotlin.coroutines.experimental.coroutineContext }
    builderSeveralArgs {_, _,_ -> kotlin.coroutines.experimental.intrinsics.<!DEPRECATION, ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!> }
    builderSeveralArgs {_, _,_ -> kotlin.coroutines.experimental.<!ILLEGAL_SUSPEND_PROPERTY_ACCESS!>coroutineContext<!> }
    builderSuspendSeveralArgs {_, _,_ -> kotlin.coroutines.experimental.intrinsics.<!DEPRECATION!>coroutineContext<!>}
    builderSuspendSeveralArgs {_, _,_ -> kotlin.coroutines.experimental.coroutineContext}
}
