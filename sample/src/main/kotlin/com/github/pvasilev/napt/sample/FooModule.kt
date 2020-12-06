package com.github.pvasilev.napt.sample

import dagger.Module
import dagger.Provides

@Module
class FooModule {

	@Provides
	fun provideFoo() = "Foo"
}