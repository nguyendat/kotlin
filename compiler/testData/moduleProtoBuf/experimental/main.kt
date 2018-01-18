package org.foo

@Experimental
annotation class A

class B {
    @Experimental
    annotation class C
}

@Experimental(ExperimentalLevel.ERROR, ExperimentalScope.SOURCE_ONLY)
annotation class D
