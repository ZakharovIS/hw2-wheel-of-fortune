package com.example.hw2_wheel

enum class SectorColor(val color: Int, val isText: Boolean) {
    RED(0xffff0000.toInt(), isText = true),
    ORANGE(0xffffa500.toInt(), isText = false),
    YELLOW(0xffffff00.toInt(), isText = true),
    GREEN(0xff008000.toInt(), isText = false),
    LIGHTBLUE(0xffadd8e6.toInt(), isText = true),
    BLUE(0xff0000ff.toInt(), isText = false),
    PURPLE(0xff800080.toInt(), isText = true);
}