package com.github.pvasilev.napt.sample

import dagger.Component

@Component(modules = [FooModule::class])
interface AppComponent {

	fun getFoo(): String
}