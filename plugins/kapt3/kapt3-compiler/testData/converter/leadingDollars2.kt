// CORRECT_ERROR_TYPES

// FILE: te/st/a/JavaClass
package te.st.a;

public class JavaClass {}

// FILE: test.kt
@file:Suppress("UNRESOLVED_REFERENCE")
package te.st.a

import te.st.a.`$Test`.Inner as MyInner

fun a(a: MyInner) {}