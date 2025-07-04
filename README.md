 fastexcel-loststyle-issue

> Demo to reproduce FastExcel style loss issue when using custom cell styles.

## 📌 问题概述

本项目用于复现 [FastExcel](https://github.com/alibaba/easyexcel) 在使用自定义样式时，**日期列样式丢失**的问题。

问题原因在于：日期字段的 Converter 会自动注册一个 `WriteCellStyle`，但若未设置 `OriginCellStyle`，则会被默认的 `FillStyleCellWriteHandler` 覆盖掉我们自定义的样式。

---

## 🚀 如何运行

使用 Maven 编译运行：

```bash
mvn compile exec:java -Dexec.mainClass="src.haceral.felsi.Main"
