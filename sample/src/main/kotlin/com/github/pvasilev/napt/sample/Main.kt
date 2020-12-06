package com.github.pvasilev.napt.sample

fun main() {
	val component = AppComponentProvider.getComponent()
	println(component.getFoo())
}