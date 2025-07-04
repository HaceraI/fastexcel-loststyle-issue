 fastexcel-loststyle-issue

> Demo to reproduce FastExcel style loss issue when using custom cell styles.

## 问题概述

本项目用于复现 [FastExcel](https://github.com/alibaba/easyexcel) 在使用自定义样式时，**日期列样式丢失**的问题。

问题原因在于：日期字段的 Converter 会自动注册一个 `WriteCellStyle`，但若未设置 `OriginCellStyle`，则会被默认的 `FillStyleCellWriteHandler` 覆盖掉我们自定义的样式。

---

## 问题根源分析
1. 用户操作：用户可能通过模板或者自定义的`CellWriteHandler`预先给单元格设置了样式（例如背景色、字体、边框等）
2. 框架转换：当写入的数据需要Converter处理时（比如LocalDate -> Date），Converter被调用
3. 样式创建：Converter内部为了应用特定的数据格式（如 yyyy-MM-dd），会调用 `WorkBookUtil.fillDataFormat()`。这个方法会创建一个全新的 `WriteCellStyle` 对象，并把它设置到 `WriteCellData` 中。关键点：此时 `WriteCellData` 中只有这个新创建的、仅包含数据格式的 `WriteCellStyle`，而 `originCellStyle` 依然是 `null`。
4. 最终应用：流程走到 `FillStyleCellWriteHandler`。它从 cellData 中取出 writeCellStyle（来自Converter）和 originCellStyle（null）。然后调用 createCellStyle 方法。由于 originCellStyle 是 null，框架无法得知单元格上已经存在的样式，最终导致的结果就是：预设的样式被Converter带来的新样式（仅含数据格式）覆盖了

---

## 如何运行

使用 Maven 编译运行：

```bash
mvn compile exec:java
```
